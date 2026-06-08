# ms-pedidos — Plaza de Comidas

Microservicio para gestión de pedidos, trazabilidad y eficiencia.

## Stack

- Java 25 + Spring Boot 4.0.6 + Maven (mvnw wrapper)
- MySQL (`plazoleta_pedidos`) + MongoDB (`plazoleta_trazabilidad`)
- Spring Security + JWT (jjwt 0.12.6, **RSA-4096 RS256** — verificación con llave pública)
- SpringDoc OpenAPI 3.0.2
- MapStruct 1.6.3 + Lombok
- Pruebas: JUnit 5 + Mockito

## Arquitectura Hexagonal

```
com.plazoleta.pedidos/
├── domain/                          # model, api (puertos entrada), spi (puertos salida), usecase
├── application/                     # handle, dto, exception, factory, mapper
└── infrastructure/
    ├── config/        BeanConfiguration, RestTemplateConfig
    ├── endpoint/      PedidoController, EficienciaController, GlobalExceptionHandler
    ├── entity/        EntidadPedido, EntidadDetallePedido (JPA)
    ├── document/      EntidadTrazabilidad (MongoDB)
    ├── persistence/   adapter, mapper (MapStruct), repository (JPA/Mongo)
    ├── security/      SecurityConfig, JwtTokenProvider, JwtAuthenticationFilter
    ├── restaurante/   RestauranteRestPedidosAdapter (RestTemplate → ms-restaurantes)
    ├── usuario/       UsuarioRestPedidosAdapter (RestTemplate → ms-usuarios)
    └── notificacion/  NotificacionRestClienteAdapter (RestTemplate → ms-notificaciones)
```

Reglas de dependencia: `infrastructure → application → domain`.

## Base de Datos

### MySQL
Esquema en `db/init.sql`. Conexión local: `root/root` en `localhost:3306/plazoleta_pedidos`.

### MongoDB
Colección `trazabilidad` — un documento por transición de estado, con `duracionEtapaMinutos`.

## Ejecución

```bash
./mvnw spring-boot:run    # Puerto 8083
./mvnw clean test         # Pruebas unitarias (41 tests)
```

## Endpoints Implementados

| Método | Ruta | Descripción | Autenticación |
|--------|------|-------------|---------------|
| POST | `/pedidos` | Crear pedido | CLIENTE |
| GET | `/pedidos?estado=&page=0&size=10` | Listar pedidos por estado | EMPLEADO |
| PATCH | `/pedidos/{id}/asignar` | Asignar pedido al empleado | EMPLEADO |
| PATCH | `/pedidos/{id}/cancelar` | Cancelar pedido | CLIENTE |
| PATCH | `/pedidos/{id}/notificar-listo` | Marcar pedido listo y notificar SMS | EMPLEADO |
| PATCH | `/pedidos/{id}/entregar` | Entregar pedido (valida PIN) | EMPLEADO |
| GET | `/pedidos/{id}/trazabilidad` | Consultar trazabilidad del pedido | CLIENTE |
| GET | `/restaurante/eficiencia` | Eficiencia del restaurante | PROPIETARIO |

Documentación OpenAPI disponible en `/swagger-ui.html` y `/v3/api-docs`.

## Notas técnicas

- `EntidadPedido` usa `@PrePersist` para asignar `fechaCreacion` y `fechaModificacion` automáticamente al insertar, y `@PreUpdate` para actualizar `fechaModificacion` en cada cambio. Esto evita `DataIntegrityViolationException` por columnas `NOT NULL` sin valor.

## Seguridad JWT

Todos los endpoints requieren un token JWT válido emitido por `ms-usuarios`. El token se envía vía header:

```
Authorization: Bearer <token>
```

- **401** — token ausente, inválido o expirado (sin body)
- **403** — token válido pero rol insuficiente (sin body)

La validación usa la **llave pública RSA-4096** (`jwt.public-key`) proporcionada por `ms-usuarios`.

---

## H11: Realizar Pedido

Crea un pedido para un restaurante. Requiere autenticación como CLIENTE.

### Request body

```json
{
  "idRestaurante": 1,
  "platos": [
    { "idPlato": 1, "cantidad": 2 }
  ]
}
```

### Validaciones de dominio

- Cliente debe existir en `ms-usuarios`
- Cliente no debe tener un pedido activo (PENDIENTE, EN_PREPARACION, LISTO)
- Restaurante debe existir
- Todos los platos deben pertenecer al restaurante y estar activos

### Respuestas

- **201**: `{ "mensaje": "Pedido realizado exitosamente" }`
- **400**: error de validación / reglas de negocio
- **404**: restaurante o plato no encontrado
- **401**: sin token o token inválido

---

## H12: Listar Pedidos por Estado

Lista los pedidos del restaurante al que está asociado el empleado autenticado. Filtro opcional por estado.

### Query params

- `estado` — opcional (PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO)
- `page` — número de página (default 0)
- `size` — tamaño de página (default 10)

### Response 200

```json
{
  "contenido": [
    {
      "id": 1,
      "idCliente": 5,
      "idRestaurante": 2,
      "estado": "PENDIENTE",
      "platos": [
        { "idPlato": 1, "cantidad": 2 }
      ]
    }
  ],
  "paginaActual": 0,
  "totalPaginas": 3,
  "totalElementos": 25
}
```

---

## H13: Asignarse Pedido

El empleado autenticado se asigna un pedido PENDIENTE de su restaurante. Cambia estado a EN_PREPARACION.

### Respuestas

- **200**: pedido asignado
- **400**: pedido no está PENDIENTE o ya tiene empleado asignado
- **403**: empleado no pertenece al restaurante del pedido
- **404**: pedido no encontrado

---

## H14: Notificar Pedido Listo

Marca el pedido como LISTO, genera un PIN de 6 dígitos y notifica al cliente vía SMS (Twilio) a través de `ms-notificaciones`.

### Respuestas

- **200**: `{ "mensaje": "Pedido listo notificado" }` (si SMS falla, pedido igual queda LISTO pero se informa del error)
- **400**: pedido no está EN_PREPARACION
- **403**: empleado no pertenece al restaurante
- **404**: pedido no encontrado

---

## H15: Entregar Pedido

El empleado entrega el pedido validando el PIN generado en H14. Cambia estado a ENTREGADO.

### Request body

```json
{
  "pin": "123456"
}
```

### Respuestas

- **200**: pedido entregado
- **400**: PIN incorrecto o pedido no está LISTO
- **403**: empleado no pertenece al restaurante
- **404**: pedido no encontrado

---

## H16: Cancelar Pedido

El cliente cancela su pedido si aún está en estado PENDIENTE.

### Respuestas

- **200**: pedido cancelado
- **400**: pedido no está PENDIENTE
- **403**: pedido no pertenece al cliente
- **404**: pedido no encontrado

---

## H17: Consultar Trazabilidad

Devuelve el historial de cambios de estado de un pedido desde MongoDB.

### Response 200

```json
{
  "idPedido": 1,
  "cambios": [
    {
      "estadoAnterior": "PENDIENTE",
      "estadoNuevo": "EN_PREPARACION",
      "fechaCambio": "2025-06-01T12:00:00",
      "duracionEtapaMinutos": 15
    }
  ]
}
```

- **404**: pedido no encontrado o no pertenece al cliente

---

## H18: Eficiencia del Restaurante

El propietario consulta el tiempo promedio de sus pedidos ENTREGADOS y un ranking de empleados por menor tiempo promedio.

### Response 200

```json
{
  "pedidos": [
    { "idPedido": 1, "tiempoTotalMinutos": 45 }
  ],
  "rankingEmpleados": [
    { "idEmpleado": 3, "tiempoPromedioMinutos": 30 }
  ]
}
```

- **404**: propietario no tiene restaurante registrado

---

## Relacion con otros microservicios

```
+------------------+     RSA-4096 RS256 (verificacion)     +------------------+
|   ms-pedidos     |  <-------------------------------------- |   ms-usuarios    |
|   Puerto 8083    |                                         |  (emisor JWT)  |
+------------------+                                         +------------------+
         |
         | RestTemplate (consulta cliente)
         v
+------------------+      RestTemplate (valida restaurante / platos info)
|  ms-restaurantes |  <----------------------------------------
|   Puerto 8082    |
+------------------+
         |
         | RestTemplate (envia notificacion SMS)
         v
+------------------+
| ms-notificaciones|
|   Puerto 8084    |
+------------------+
```

| Microservicio | Relacion | Como interactua |
|---------------|----------|-----------------|
| **ms-usuarios** | **Valida JWT + consulta cliente** | `ms-pedidos` valida el JWT localmente con llave publica RSA. Consulta `GET /usuarios/{id}` para obtener nombre y celular del cliente al crear un pedido. |
| **ms-restaurantes** | **Valida negocio** | `ms-pedidos` consulta `GET /restaurantes/{id}` para validar existencia del restaurante, y `POST /restaurantes/{id}/platos/info` para validar pertenencia/activos de platos y obtener sus nombres. |
| **ms-notificaciones** | **Notifica cliente** | `ms-pedidos` llama a `POST /notificaciones/enviar` para enviar SMS con el PIN de entrega al cliente, forwardeando el JWT del empleado. |

### Flujo de creacion de pedido (H11)

1. CLIENTE autenticado -> `POST /pedidos` en **ms-pedidos**
2. `ms-pedidos` -> `GET /usuarios/{idCliente}` en **ms-usuarios** (obtiene nombre y celular)
3. `ms-pedidos` -> `GET /restaurantes/{id}` en **ms-restaurantes** (valida existencia del restaurante)
4. `ms-pedidos` -> `POST /restaurantes/{id}/platos/info` en **ms-restaurantes** (valida que platos pertenezcan al restaurante, esten activos, y obtiene nombres)
5. Si todas las validaciones OK, persiste el pedido en MySQL y guarda trazabilidad `null -> PENDIENTE` en MongoDB

### Flujo de notificacion (H14)

1. EMPLEADO autenticado -> `PATCH /pedidos/{id}/notificar-listo` en **ms-pedidos**
2. `ms-pedidos` cambia estado a LISTO, genera PIN de 6 digitos
3. `ms-pedidos` -> `POST /notificaciones/enviar` en **ms-notificaciones** (forwardeando JWT)
4. `ms-notificaciones` envia SMS via Twilio y guarda log en MongoDB
5. Si SMS falla, el pedido igual queda LISTO pero se informa del error

### Flujo de entrega (H15)

1. EMPLEADO autenticado -> `PATCH /pedidos/{id}/entregar` con PIN en **ms-pedidos**
2. `ms-pedidos` valida que el PIN coincida con el generado en H14
3. Si OK, cambia estado a ENTREGADO y guarda trazabilidad `LISTO -> ENTREGADO` en MongoDB

### Flujo de cancelacion (H16)

1. CLIENTE autenticado -> `PATCH /pedidos/{id}/cancelar` en **ms-pedidos**
2. `ms-pedidos` valida que el pedido pertenezca al cliente y este en PENDIENTE
3. Si OK, cambia estado a CANCELADO y guarda trazabilidad `PENDIENTE -> CANCELADO` en MongoDB

### Estados del pedido

```
PENDIENTE --(H13: asignar)--> EN_PREPARACION --(H14: notificar)--> LISTO --(H15: entregar)--> ENTREGADO
     |
     |--(H16: cancelar)--> CANCELADO
```
