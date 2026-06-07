# Task Record — HU-17: Consultar Trazabilidad

## Microservicio
**ms-pedidos** (Puerto 8083, MongoDB `plazoleta_trazabilidad`)

## Rama
`feature/H17-consultar-trazabilidad`

## Archivos creados

### Domain
- `src/main/java/com/plazoleta/pedidos/domain/model/Trazabilidad.java`
- `src/main/java/com/plazoleta/pedidos/domain/api/ConsultarTrazabilidadPort.java`
- `src/main/java/com/plazoleta/pedidos/domain/usecase/ConsultarTrazabilidad.java`
- `src/main/java/com/plazoleta/pedidos/domain/spi/TrazabilidadRepositoryPort.java`

### Application
- `src/main/java/com/plazoleta/pedidos/application/dto/TrazabilidadResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/dto/CambioEstadoResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/handle/ConsultarTrazabilidadHandle.java`

### Infrastructure
- `src/main/java/com/plazoleta/pedidos/infrastructure/document/EntidadTrazabilidad.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/document/ITrazabilidadMongoRepository.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/document/TrazabilidadRepositoryAdapter.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/endpoint/PedidoController.java` — `@GetMapping("/{id}/trazabilidad")`

### Test
- `src/test/java/com/plazoleta/pedidos/domain/usecase/ConsultarTrazabilidadTest.java` — 4 tests

## Resultado de pruebas

```bash
./mvnw clean test
# Tests run: 31, Failures: 0, Errors: 0, Skipped: 0
# BUILD SUCCESS
```

## Commits
1. `feat(trazabilidad): entidad MongoDB, puerto y adaptador de trazabilidad`
2. `feat(trazabilidad): endpoint GET /pedidos/{id}/trazabilidad`
3. `test(trazabilidad): pruebas unitarias (4 escenarios)`
