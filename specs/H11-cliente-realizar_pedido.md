# HU-11: Realizar pedido

## Historia de usuario

> **Rol:** Cliente de la plazoleta de comidas
> **Funcionalidad:** Solicitar los platos de mi preferencia
> **Motivo:** Que puedan prepararlos y traerlos a mi mesa

---

## Criterios de aceptación

### Campos de la solicitud

| Campo | Tipo | Descripción |
|---|---|---|
| idRestaurante | Numérico | Restaurante al que se hace el pedido |
| platos | Lista | Lista de platos solicitados |
| └ idPlato | Numérico | Plato solicitado |
| └ cantidad | Numérico entero | Cantidad de ese plato, mayor a 0 |

### Reglas de negocio

- Un pedido consta de una lista de platos de un **mismo restaurante**.
- Inmediatamente después de recibir un pedido, este queda con estado **PENDIENTE**.
- El cliente puede solicitar un nuevo pedido **solo si no tiene ningún pedido en proceso** (PENDIENTE, EN_PREPARACION o LISTO).
- El pedido se asocia automáticamente al cliente autenticado.
- Se debe validar que el restaurante exista.
- Se debe validar que todos los platos pertenezcan al restaurante especificado.
- Se debe validar que todos los platos estén activos (`activo = true`).
- La cantidad de cada plato debe ser un número entero positivo mayor a 0.

### Respuestas del sistema

- **Pedido exitoso:** El sistema responde solo con un mensaje de éxito (sin id del pedido).
- **Error de validación:** El sistema responde con el error correspondiente.

---

## Endpoints (propuesta inicial)

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/pedidos` | Realizar pedido |

### Request body (JSON)

```json
{
  "idRestaurante": 1,
  "platos": [
    {
      "idPlato": 1,
      "cantidad": 2
    }
  ]
}
```

### Response 201 — Pedido creado exitosamente

```json
{
  "mensaje": "Pedido realizado exitosamente"
}
```

### Response 400 — Error de validación

```json
{
  "mensaje": "Tienes un pedido en proceso"
}
```

```json
{
  "mensaje": "El plato no pertenece al restaurante"
}
```

```json
{
  "mensaje": "El plato {nombre} no se encuentra disponible"
}
```

### Response 401 — No autenticado

*(Sin cuerpo en la respuesta)*

### Response 404 — Restaurante no encontrado

```json
{
  "mensaje": "El restaurante no existe"
}
```

---

## Suposiciones validadas

1. ✅ El cliente envía la solicitud a través de servicios REST (sin frontend).
2. ✅ Si el pedido se crea correctamente, el sistema responde solo con un mensaje de éxito.
3. ✅ Si hay errores de validación, el sistema responde con el error correspondiente.
4. ✅ El cliente debe estar autenticado y tener el rol CLIENTE.
5. ✅ El pedido se asocia automáticamente al cliente autenticado.
6. ✅ Se debe validar que el restaurante exista.
7. ✅ Se debe validar que todos los platos pertenezcan al restaurante especificado.
8. ✅ Se debe validar que todos los platos estén activos.
9. ✅ La cantidad de cada plato debe ser un número entero positivo mayor a 0.
10. ✅ Si el cliente ya tiene un pedido en proceso, el sistema rechaza el nuevo pedido.
