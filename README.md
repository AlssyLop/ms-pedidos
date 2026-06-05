# ms-pedidos — Plaza de Comidas

Microservicio para gestión de pedidos.

## Stack
- Java 25 + Spring Boot 4.0.6 + Maven
- MySQL (`plazoleta_pedidos`) + MongoDB (`plazoleta_trazabilidad`)
- Spring Security + JWT (jjwt 0.12.6)
- MapStruct 1.6.3

## Endpoints

### H11 — Realizar pedido
**POST** `/pedidos` (CLIENTE)

```json
{
  "idRestaurante": 1,
  "platos": [{ "idPlato": 1, "cantidad": 2 }]
}
```
- Valida que el cliente no tenga pedido activo (PENDIENTE, EN_PREPARACION, LISTO)
- Valida que el restaurante exista
- Valida que todos los platos pertenezcan al restaurante y estén activos
- Crea pedido con estado PENDIENTE

**Respuestas:**
- 201: `{ "mensaje": "Pedido realizado exitosamente" }`
- 400: `{ "mensaje": "..." }` (reglas de negocio)
- 404: `{ "mensaje": "El restaurante no existe" }`
- 401: *(sin body)*

## Arquitectura
```
com.plazoleta.pedidos/
├── domain/        # model, spi (puertos), api, usecase
├── application/   # handle, dto, exception, factory
└── infrastructure/
    ├── config/    # BeanConfiguration, RestTemplateConfig
    ├── entity/    # EntidadPedido, EntidadDetallePedido (JPA)
    ├── document/  # EntidadTrazabilidad (MongoDB)
    ├── persistence/ # adapter, mapper (MapStruct), repository
    ├── security/  # SecurityConfig, JwtTokenProvider, JwtAuthenticationFilter
    ├── restaurante/ # RestauranteRestPedidosAdapter (RestTemplate)
    ├── usuario/   # UsuarioRestPedidosAdapter (RestTemplate)
    └── endpoint/  # controller, exception handler
```

## Configuración
```properties
server.port=8083
jwt.secret=claveSecreta256BitsParaJWTTokenDeAccesoDelSistemaPlazoleta
ms-restaurantes.url=http://localhost:8082
ms-usuarios.url=http://localhost:8081
```

## Pruebas
```bash
./mvnw clean test
```
- Tests: 7 (BUILD SUCCESS)