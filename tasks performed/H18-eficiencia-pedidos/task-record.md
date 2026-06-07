# Task Record — HU-18: Eficiencia Pedidos

## Microservicio
**ms-pedidos** (Puerto 8083, MongoDB `plazoleta_trazabilidad`)

## Rama
`feature/H18-eficiencia-pedidos`

## Archivos creados

### Domain
- `src/main/java/com/plazoleta/pedidos/domain/model/Eficiencia.java`
- `src/main/java/com/plazoleta/pedidos/domain/model/RankingEmpleado.java`
- `src/main/java/com/plazoleta/pedidos/domain/model/PedidoEficiencia.java`
- `src/main/java/com/plazoleta/pedidos/domain/api/ConsultarEficienciaPort.java`
- `src/main/java/com/plazoleta/pedidos/domain/usecase/ConsultarEficiencia.java`
- `src/main/java/com/plazoleta/pedidos/domain/spi/PropietarioRestaurantePort.java`

### Application
- `src/main/java/com/plazoleta/pedidos/application/dto/EficienciaResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/dto/RankingEmpleadoResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/dto/PedidoEficienciaResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/handle/ConsultarEficienciaHandle.java`

### Infrastructure
- `src/main/java/com/plazoleta/pedidos/infrastructure/endpoint/EficienciaController.java` — `@GetMapping("/eficiencia")`
- `src/main/java/com/plazoleta/pedidos/infrastructure/restaurante/RestauranteInfoResponse.java`

## Archivos modificados
- `infrastructure/config/BeanConfiguration.java` — beans Eficiencia y PropietarioRestaurantePort

### Test
- `src/test/java/com/plazoleta/pedidos/domain/usecase/ConsultarEficienciaTest.java` — 4 tests

## Resultado de pruebas

```bash
./mvnw clean test
# Tests run: 35, Failures: 0, Errors: 0, Skipped: 0
# BUILD SUCCESS
```

## Commits
1. `feat(eficiencia): modelo, puerto y caso de uso para métricas de eficiencia`
2. `feat(eficiencia): endpoint GET /restaurante/eficiencia y adaptador propietario-restaurante`
3. `test(eficiencia): pruebas unitarias (4 escenarios)`
