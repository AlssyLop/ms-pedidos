# HU-13: Asignarse a un pedido y cambiar estado a "en preparación"

## Historia de usuario

> **Rol:** Empleado de un restaurante
> **Funcionalidad:** Asignarme a un pedido y cambiarle su estado
> **Motivo:** Que el cliente pueda saber el avance de su orden

---

## Criterios de aceptación

### Asignación a pedido

| Campo | Descripción |
|---|---|
| Id del pedido | En la URL para identificar el pedido |

### Reglas de negocio

- El empleado se asigna a un pedido enviando el **id del pedido en la URL**.
- Al asignarse, el estado del pedido cambia automáticamente de **PENDIENTE** a **EN_PREPARACION**.
- El id del empleado se obtiene del **token de autorización** (no se envía en el cuerpo de la petición).
- El empleado solo puede asignarse a pedidos del restaurante al que pertenece.
- Solo se permite asignarse a pedidos que estén en estado **PENDIENTE**.
- Si el pedido ya tiene otro empleado asignado, el sistema rechaza la operación.
- El empleado debe estar autenticado y tener el rol **EMPLEADO**.

### Respuestas del sistema

- **Asignación exitosa:** El sistema responde con un mensaje de confirmación.
- **Error:** Si el pedido no está en estado PENDIENTE, o ya tiene empleado asignado, o no pertenece al restaurante del empleado.

---

## Endpoints (propuesta inicial)

| Método | Ruta | Descripción |
|---|---|---|
| `PATCH` | `pedidos/{id}/asignar` | Asignarse a un pedido y pasar a EN_PREPARACION |

### Request

Sin cuerpo — el id del empleado se obtiene del token de autorización.

### Response 200 — Asignación exitosa

```json
{
  "mensaje": "Pedido asignado exitosamente"
}
```

### Response 400 — Error de validación

```json
{
  "mensaje": "Pedido asignado, se encuentra en estado PREPARACION"
}
```

```json
{
  "mensaje": "El pedido ya tiene un empleado asignado"
}
```

### Response 401 — No autenticado

*(Sin cuerpo en la respuesta)*

### Response 403 — No autorizado

```json
{
  "mensaje": "No tienes permiso para asignarte a este pedido"
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

1. ✅ El empleado se asigna a un pedido enviando el id del pedido en la URL.
2. ✅ Al asignarse, el estado cambia automáticamente de PENDIENTE a EN_PREPARACION.
3. ✅ Si el pedido no está en PENDIENTE, el sistema rechaza la asignación.
4. ✅ El empleado solo puede asignarse a pedidos de su propio restaurante.
5. ✅ Si el pedido ya tiene otro empleado asignado, el sistema rechaza la operación.
6. ✅ El sistema responde con un mensaje de confirmación si la asignación es exitosa.
7. ✅ El empleado debe estar autenticado y tener el rol EMPLEADO.
8. ✅ El token contiene la información del usuario (id, rol, correo) — el servidor extrae el id del empleado del token.
