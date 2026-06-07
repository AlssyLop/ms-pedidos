# Task Record — HU-14: Notificar Pedido Listo

## Microservicio
**ms-pedidos** (Puerto 8083)

## Rama
`feature/H14-notificar-pedido-listo`

## Archivos creados

### Domain
- `src/main/java/com/plazoleta/pedidos/domain/api/NotificarPedidoListoPort.java`
- `src/main/java/com/plazoleta/pedidos/domain/usecase/NotificarPedidoListo.java`
- `src/main/java/com/plazoleta/pedidos/domain/spi/NotificacionClientePort.java`

### Application
- `src/main/java/com/plazoleta/pedidos/application/dto/NotificarPedidoListoResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/handle/NotificarPedidoListoHandle.java`

### Infrastructure
- `src/main/java/com/plazoleta/pedidos/infrastructure/notificacion/NotificacionRestClienteAdapter.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/endpoint/PedidoController.java` — `@PatchMapping("/{id}/notificar-listo")`

### Test
- `src/test/java/com/plazoleta/pedidos/domain/usecase/NotificarPedidoListoTest.java` — 6 tests

## Resultado de pruebas

```bash
./mvnw clean test
# Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
# BUILD SUCCESS
```

## Commits
1. `feat(notificar-listo): puerto, caso de uso y adaptador a ms-notificaciones`
2. `feat(notificar-listo): endpoint PATCH /pedidos/{id}/notificar-listo`
3. `test(notificar-listo): pruebas unitarias (6 escenarios)`
