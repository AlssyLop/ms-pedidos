# HU-15: Entregar pedido (Marcarlo como entregado)

## Historia de usuario

> **Rol:** Empleado de un restaurante
> **Funcionalidad:** Marcar como entregado un pedido
> **Motivo:** Poder cerrar el ciclo del mismo y concentrarme en otros pedidos

---

## Criterios de aceptación

### Campos de la solicitud

| Campo | Ubicación | Descripción |
|---|---|---|
| Id del pedido | URL | Identificador del pedido a entregar |
| Pin de seguridad | Cuerpo (JSON) | Pin enviado al cliente para reclamar su pedido |

### Reglas de negocio

- Solo los pedidos en estado **LISTO** pueden pasar a estado **ENTREGADO**.
- Ningún pedido en estado ENTREGADO puede modificarse a ningún otro estado (es un estado terminal).
- Para cambiar a ENTREGADO, el empleado debe digitar el **pin de seguridad** que le fue enviado al cliente.
- El sistema valida que el pin ingresado coincida con el pin generado cuando el pedido pasó a LISTO.
- Si el pin es incorrecto, el sistema rechaza la operación.
- El empleado debe estar autenticado y tener el rol **EMPLEADO**.
- Solo se pueden entregar pedidos del restaurante al que pertenece el empleado.
- El id del empleado se obtiene del token de autorización.

### Respuestas del sistema

- **Entrega exitosa:** El sistema responde con un mensaje de confirmación.
- **Error:** Pin incorrecto, pedido no está en LISTO, o no pertenece al restaurante.

---

## Endpoints (propuesta inicial)

| Método | Ruta | Descripción |
|---|---|---|
| `PATCH` | `pedidos/{id}/entregar` | Entregar pedido |

### Request body (JSON)

```json
{
  "pin": "string"
}
```

### Response 200 — Entrega exitosa

```json
{
  "mensaje": "Pedido entregado exitosamente"
}
```

### Response 400 — Error de validación

```json
{
  "mensaje": "El pedido no se encuentra en estado LISTO"
}
```

```json
{
  "mensaje": "El pin de seguridad es incorrecto"
}
```

### Response 401 — No autenticado

*(Sin cuerpo en la respuesta)*

### Response 403 — No autorizado

```json
{
  "mensaje": "No tienes permiso para entregar este pedido"
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

1. ✅ El empleado envía el id del pedido en la URL y el pin en el cuerpo.
2. ✅ El sistema valida que el pin coincida con el generado al marcar LISTO.
3. ✅ Si el pin es correcto, el pedido cambia de LISTO a ENTREGADO.
4. ✅ Si el pin es incorrecto, el sistema rechaza la operación.
5. ✅ Una vez ENTREGADO, el pedido no puede modificarse a ningún otro estado.
6. ✅ El empleado debe estar autenticado y tener el rol EMPLEADO.
7. ✅ Solo se pueden entregar pedidos del restaurante del empleado.
8. ✅ El sistema responde con un mensaje de confirmación.
