# Task Record — HU-13: Asignarse Pedido

## Microservicio
**ms-pedidos** (Puerto 8083, MySQL `plazoleta_pedidos`)

## Rama
`feature/H11-realizar-pedido` (compartida con H11, H12, H13)

## Archivos creados

### Domain
- `src/main/java/com/plazoleta/pedidos/domain/api/AsignarPedidoPort.java`
- `src/main/java/com/plazoleta/pedidos/domain/usecase/AsignarPedido.java`

### Application
- `src/main/java/com/plazoleta/pedidos/application/dto/AsignarPedidoResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/handle/AsignarPedidoHandle.java`

### Infrastructure
- `src/main/java/com/plazoleta/pedidos/infrastructure/endpoint/PedidoController.java` — `@PatchMapping("/{id}/asignar")`

### Test
- `src/test/java/com/plazoleta/pedidos/domain/usecase/AsignarPedidoTest.java` — 6 tests

## Resultado de pruebas

```bash
./mvnw clean test
# Tests run: 16, Failures: 0, Errors: 0, Skipped: 0
# BUILD SUCCESS
```

## Commits
1. `feat(asignar-pedido): puerto, caso de uso y handle para asignación`
2. `feat(asignar-pedido): endpoint PATCH /pedidos/{id}/asignar`
3. `test(asignar-pedido): pruebas unitarias (6 escenarios)`
