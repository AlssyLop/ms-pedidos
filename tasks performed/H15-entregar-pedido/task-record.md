# Task Record — HU-15: Entregar Pedido

## Microservicio
**ms-pedidos** (Puerto 8083, MySQL `plazoleta_pedidos`)

## Rama
`feature/H11-realizar-pedido` (compartida con H11, H12, H13, H15, H16)

## Archivos creados

### Domain
- `src/main/java/com/plazoleta/pedidos/domain/api/EntregarPedidoPort.java`
- `src/main/java/com/plazoleta/pedidos/domain/usecase/EntregarPedido.java`

### Application
- `src/main/java/com/plazoleta/pedidos/application/dto/EntregarPedidoRequest.java`
- `src/main/java/com/plazoleta/pedidos/application/dto/EntregarPedidoResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/handle/EntregarPedidoHandle.java`

### Infrastructure
- `src/main/java/com/plazoleta/pedidos/infrastructure/endpoint/PedidoController.java` — `@PatchMapping("/{id}/entregar")`

### Test
- `src/test/java/com/plazoleta/pedidos/domain/usecase/EntregarPedidoTest.java` — 7 tests

## Resultado de pruebas

```bash
./mvnw clean test
# Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
# BUILD SUCCESS
```

## Commits
1. `feat(entregar-pedido): puerto, caso de uso y DTOs de entrega con PIN`
2. `feat(entregar-pedido): endpoint PATCH /pedidos/{id}/entregar`
3. `test(entregar-pedido): pruebas unitarias (7 escenarios)`
