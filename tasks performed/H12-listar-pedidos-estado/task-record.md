# Task Record — HU-12: Listar Pedidos por Estado

## Microservicio
**ms-pedidos** (Puerto 8083, MySQL `plazoleta_pedidos`)

## Rama
`feature/H11-realizar-pedido` (compartida con H11, H12, H13)

## Archivos creados

### Domain
- `src/main/java/com/plazoleta/pedidos/domain/api/ListarPedidosPorEstadoPort.java`
- `src/main/java/com/plazoleta/pedidos/domain/usecase/ListarPedidosPorEstado.java`

### Application
- `src/main/java/com/plazoleta/pedidos/application/dto/PedidoListadoResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/dto/PlatoPedidoListadoResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/dto/PedidoPageResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/handle/ListarPedidosPorEstadoHandle.java`

### Infrastructure
- `src/main/java/com/plazoleta/pedidos/infrastructure/restaurante/EmpleadoRestaurantePedidosAdapter.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/restaurante/EmpleadoRestauranteInfoResponse.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/endpoint/PedidoController.java` — `@GetMapping`

### Test
- `src/test/java/com/plazoleta/pedidos/domain/usecase/ListarPedidosPorEstadoTest.java` — 3 tests

## Resultado de pruebas

```bash
./mvnw clean test
# Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
# BUILD SUCCESS
```

## Commits
1. `feat(listar-pedidos): puerto, caso de uso y DTOs de listado paginado`
2. `feat(listar-pedidos): endpoint GET /pedidos con filtro estado y adaptador empleado-restaurante`
3. `test(listar-pedidos): pruebas unitarias (3 escenarios)`
