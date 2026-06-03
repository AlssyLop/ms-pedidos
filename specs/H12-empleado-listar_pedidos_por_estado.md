# HU-12: Obtener lista de pedidos filtrando por estado

## Historia de usuario

> **Rol:** Empleado de un restaurante
> **Funcionalidad:** Visualizar la lista de pedidos filtrando por estado
> **Motivo:** Poder seleccionar aquel pedido al que necesite cambiarle su estado

---

## Criterios de aceptación

### Parámetros de consulta

| Parámetro | Obligatorio | Descripción |
|---|---|---|
| `estado` | No | Filtro por estado del pedido (PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO) |
| `page` | No | Número de página a consultar. Por defecto 0 |
| `size` | No | Cantidad de elementos por página. Por defecto 10 |

### Reglas de negocio

- El servicio devuelve la lista de pedidos del restaurante al que pertenece el empleado.
- El filtro por estado se envía como parámetro en la URL.
- Solo se puede filtrar por **un estado a la vez**.
- Si no se envía filtro de estado, se devuelven todos los pedidos del restaurante sin importar su estado.
- La respuesta es **paginada**. El empleado especifica el **número de página** y la **cantidad de elementos por página** como parámetros opcionales (por defecto page=0, size=10).
- La respuesta incluye todos los campos del pedido más información de paginación.
- El empleado debe estar autenticado y tener el rol **EMPLEADO**.

### Respuestas del sistema

- **Listado exitoso:** El sistema responde con la página solicitada de pedidos más información de paginación.
- **No autenticado:** 401 Unauthorized sin mensaje.

---

## Endpoints (propuesta inicial)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `pedidos?estado=pendiente&page=0&size=10` | Listar pedidos por estado |

### Response 200 — Listado exitoso

```json
{
  "contenido": [
    {
      "id": 1,
      "idCliente": 1,
      "nombreCliente": "string",
      "celular": "string"
      "idRestaurante": 1,
      "estado": "PENDIENTE",
      "platos": [
        {
          "idPlato": 1,
          "nombre": "string",
          "cantidad": 2
        }
      ],
      "fechaCreacion": "DD/MM/AAAA HH:mm:ss"
    }
  ],
  "paginaActual": 0,
  "totalPaginas": 3,
  "totalElementos": 25
}
```

### Response 401 — No autenticado

*(Sin cuerpo en la respuesta)*

---

## Suposiciones validadas

1. ✅ El empleado envía la solicitud a través de servicios REST (sin frontend).
2. ✅ El empleado debe estar autenticado y tener el rol EMPLEADO.
3. ✅ El filtro por estado se envía como parámetro en la URL.
4. ✅ El empleado especifica el número de página y la cantidad de elementos por página como parámetros opcionales (por defecto page=0, size=10).
5. ✅ La respuesta incluye información de paginación (página actual, total de páginas, total de elementos).
6. ✅ Si no se envía filtro de estado, se devuelven todos los pedidos del restaurante.
7. ✅ Solo se puede filtrar por un estado a la vez.
