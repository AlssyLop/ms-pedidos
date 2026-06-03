# HU-18: Consultar la eficiencia de los pedidos

## Historia de usuario

> **Rol:** Propietario de un restaurante
> **Funcionalidad:** Saber qué eficiencia están teniendo los pedidos del restaurante
> **Motivo:** Dar una mejor experiencia a los clientes

---

## Criterios de aceptación

### Reglas de negocio

- "Inicia" se refiere a cuando el pedido se crea (fechaCreación, estado PENDIENTE).
- "Termina" se refiere a cuando el pedido se marca como **ENTREGADO**.
- El tiempo se calcula en **minutos** entre la creación y la entrega del pedido.
- La información se obtiene de los registros de trazabilidad almacenados en **MongoDB**.
- El propietario solo puede ver los pedidos de su propio restaurante (se obtiene de su token).
- El ranking de empleados se muestra ordenado del más rápido al más lento (menor tiempo promedio primero).
- El propietario debe estar autenticado y tener el rol **PROPIETARIO**.

### Respuestas del sistema

- **Consulta exitosa:** Lista de pedidos con su tiempo individual y ranking de empleados con tiempo promedio.
- **Error:** Si el propietario no tiene restaurante o no hay pedidos.

---

## Endpoints (propuesta inicial)

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/restaurante/eficiencia` | Consultar eficiencia de pedidos del restaurante |

### Response 200 — Eficiencia encontrada

```json
{
  "pedidos": [
    {
      "idPedido": 1,
      "idEmpleado": 2,
      "nombreEmpleado": "string",
      "tiempoTotalMinutos": 25,
      "fechaInicio": "DD/MM/AAAA HH:mm:ss",
      "fechaFin": "DD/MM/AAAA HH:mm:ss"
    },
    {
      "idPedido": 2,
      "idEmpleado": 3,
      "nombreEmpleado": "string",
      "tiempoTotalMinutos": 40,
      "fechaInicio": "DD/MM/AAAA HH:mm:ss",
      "fechaFin": "DD/MM/AAAA HH:mm:ss"
    }
  ],
  "rankingEmpleados": [
    {
      "idEmpleado": 2,
      "nombreEmpleado": "string",
      "tiempoPromedioMinutos": 22.5
    },
    {
      "idEmpleado": 3,
      "nombreEmpleado": "string",
      "tiempoPromedioMinutos": 35.0
    }
  ]
}
```

### Response 401 — No autenticado

*(Sin cuerpo en la respuesta)*

### Response 404 — Sin datos

```json
{
  "mensaje": "No hay pedidos entregados para calcular eficiencia"
}
```

---

## Suposiciones validadas

1. ✅ "Inicia" = creación del pedido (PENDIENTE).
2. ✅ "Termina" = pedido marcado como ENTREGADO.
3. ✅ Tiempo calculado en minutos entre creación y entrega.
4. ✅ Datos obtenidos de trazabilidad en MongoDB.
5. ✅ El propietario solo ve pedidos de su restaurante.
6. ✅ Ranking de empleados ordenado del más rápido al más lento.
7. ✅ Se devuelven pedidos individuales + ranking de empleados.
8. ✅ El propietario debe estar autenticado y tener el rol PROPIETARIO.
