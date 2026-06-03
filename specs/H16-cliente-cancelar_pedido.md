# HU-16: Cancelar pedido

## Historia de usuario

> **Rol:** Cliente de un restaurante
> **Funcionalidad:** Cancelar mi pedido
> **Motivo:** Tener la posibilidad de retractarme por cualquier motivo

---

## Criterios de aceptación

### Reglas de negocio

- Solo se pueden cancelar pedidos que se encuentren en estado **PENDIENTE**.
- Si el pedido está en otro estado diferente a PENDIENTE, se notifica al usuario: *"Lo sentimos, tu pedido ya está en preparación y no puede cancelarse"*.
- El cliente envía el **id del pedido en la URL** para cancelarlo.
- El cliente solo puede cancelar **sus propios pedidos** (se obtiene de su token).
- Una vez cancelado, el cliente puede realizar un nuevo pedido.
- El cliente debe estar autenticado y tener el rol **CLIENTE**.

### Respuestas del sistema

- **Cancelación exitosa:** El sistema responde con un mensaje de confirmación.
- **Error:** Pedido no está en PENDIENTE, o no pertenece al cliente.

---

## Endpoints (propuesta inicial)

| Método | Ruta | Descripción |
|---|---|---|
| `PATCH` | `pedidos/{id}/cancelar` | Cancelar pedido |

### Request

Sin cuerpo — el id del cliente se obtiene del token de autorización.

### Response 200 — Cancelación exitosa

```json
{
  "mensaje": "Pedido cancelado exitosamente"
}
```

### Response 400 — Error de validación

```json
{
  "mensaje": "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse"
}
```

### Response 401 — No autenticado

*(Sin cuerpo en la respuesta)*

### Response 403 — No autorizado

```json
{
  "mensaje": "No tienes permiso para cancelar este pedido"
}
```

### Response 404 — Pedido no encontrado

```json
{
  "mensaje": "El pedido no existe"
}
```

---

## Suposiciones validadas

1. ✅ El cliente envía el id del pedido en la URL.
2. ✅ El cliente solo puede cancelar sus propios pedidos.
3. ✅ Si está en PENDIENTE, se cancela exitosamente.
4. ✅ Si está en otro estado, se muestra el mensaje correspondiente.
5. ✅ Una vez cancelado, el cliente puede realizar un nuevo pedido.
6. ✅ El sistema responde con un mensaje de confirmación.
7. ✅ El cliente debe estar autenticado y tener el rol CLIENTE.
