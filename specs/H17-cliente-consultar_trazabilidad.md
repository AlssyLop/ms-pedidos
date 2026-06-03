# HU-17: Consultar trazabilidad del pedido

## Historia de usuario

> **Rol:** Cliente de un restaurante
> **Funcionalidad:** Visualizar los cambios de estado de mi pedido
> **Motivo:** Ver la rapidez del servicio solicitado

---

## Criterios de aceptación

### Reglas de negocio

- Se crea un **registro por cada cambio de estado** de un pedido: estado anterior, estado nuevo, fecha y hora del cambio.
- El historial de cambios se almacena en **MongoDB** (base de datos no relacional).
- Cada cliente puede consultar el historial solo de **sus propios pedidos**.
- El cliente envía el **id del pedido en la URL**.
- Los cambios se devuelven **ordenados cronológicamente**.
- El cliente debe estar autenticado y tener el rol **CLIENTE**.

### Respuestas del sistema

- **Consulta exitosa:** Lista de cambios de estado del pedido.
- **Error:** Pedido no encontrado, o no pertenece al cliente.

---

## Endpoints (propuesta inicial)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `pedidos/{id}/trazabilidad` | Consultar trazabilidad del pedido |

### Response 200 — Trazabilidad encontrada

```json
{
  "idPedido": 1,
  "cambios": [
    {
      "estadoAnterior": null,
      "estadoNuevo": "PENDIENTE",
      "fechaCambio": "DD/MM/AAAA HH:mm:ss"
    },
    {
      "estadoAnterior": "PENDIENTE",
      "estadoNuevo": "EN_PREPARACION",
      "fechaCambio": "DD/MM/AAAA HH:mm:ss"
    },
    {
      "estadoAnterior": "EN_PREPARACION",
      "estadoNuevo": "LISTO",
      "fechaCambio": "DD/MM/AAAA HH:mm:ss"
    },
    {
      "estadoAnterior": "LISTO",
      "estadoNuevo": "ENTREGADO",
      "fechaCambio": "DD/MM/AAAA HH:mm:ss"
    }
  ]
}
```

### Response 401 — No autenticado

*(Sin cuerpo en la respuesta)*

### Response 403 — No autorizado

```json
{
  "mensaje": "No tienes permiso para consultar este pedido"
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

1. ✅ Cada cambio de estado guarda: estado anterior, estado nuevo, fecha y hora.
2. ✅ El historial se almacena en MongoDB.
3. ✅ El cliente envía el id del pedido en la URL.
4. ✅ La respuesta incluye todos los cambios ordenados cronológicamente.
5. ✅ El cliente solo puede ver trazabilidad de sus propios pedidos.
6. ✅ El cliente debe estar autenticado y tener el rol CLIENTE.
7. ✅ El sistema responde con la lista de cambios de estado.
