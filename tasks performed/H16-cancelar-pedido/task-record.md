# Task Record — HU-16: Cancelar Pedido

## Microservicio
**ms-pedidos** (Puerto 8083, MySQL `plazoleta_pedidos`)

## Rama
`feature/H11-realizar-pedido` (compartida con H11, H12, H13, H15, H16)

## Archivos creados

### Domain
- `src/main/java/com/plazoleta/pedidos/domain/api/CancelarPedidoPort.java`
- `src/main/java/com/plazoleta/pedidos/domain/usecase/CancelarPedido.java`

### Application
- `src/main/java/com/plazoleta/pedidos/application/dto/CancelarPedidoResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/handle/CancelarPedidoHandle.java`

### Infrastructure
- `src/main/java/com/plazoleta/pedidos/infrastructure/endpoint/PedidoController.java` — `@PatchMapping("/{id}/cancelar")`

### Test
- `src/test/java/com/plazoleta/pedidos/domain/usecase/CancelarPedidoTest.java` — 4 tests

## Resultado de pruebas

```bash
./mvnw clean test
# Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
# BUILD SUCCESS
```

## Commits
1. `feat(cancelar-pedido): puerto, caso de uso y handle para cancelación`
2. `feat(cancelar-pedido): endpoint PATCH /pedidos/{id}/cancelar`
3. `test(cancelar-pedido): pruebas unitarias (4 escenarios)`
