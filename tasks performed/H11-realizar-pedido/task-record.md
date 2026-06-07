# Task Record — HU-11: Realizar Pedido

## Microservicio
**ms-pedidos** (Puerto 8083, MySQL `plazoleta_pedidos` + MongoDB `plazoleta_trazabilidad`)

## Rama
`feature/H11-realizar-pedido` (desde `master`)

## Archivos creados

### Domain
- `src/main/java/com/plazoleta/pedidos/domain/model/Pedido.java`
- `src/main/java/com/plazoleta/pedidos/domain/model/DetallePedido.java`
- `src/main/java/com/plazoleta/pedidos/domain/model/EstadoPedido.java`
- `src/main/java/com/plazoleta/pedidos/domain/api/CrearPedidoPort.java`
- `src/main/java/com/plazoleta/pedidos/domain/usecase/CrearPedido.java`

### Application
- `src/main/java/com/plazoleta/pedidos/application/dto/PedidoRequest.java`
- `src/main/java/com/plazoleta/pedidos/application/dto/PlatoPedidoRequest.java`
- `src/main/java/com/plazoleta/pedidos/application/dto/PedidoResponse.java`
- `src/main/java/com/plazoleta/pedidos/application/factory/PedidoFactory.java`
- `src/main/java/com/plazoleta/pedidos/application/handle/CrearPedidoHandle.java`
- `src/main/java/com/plazoleta/pedidos/application/exception/PedidoNoEncontradoException.java`
- `src/main/java/com/plazoleta/pedidos/application/exception/ValidacionException.java`

### Infrastructure
- `src/main/java/com/plazoleta/pedidos/infrastructure/entity/EntidadPedido.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/entity/EntidadDetallePedido.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/entity/EstadoPedidoEntity.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/persistence/repository/IPedidoJpaRepository.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/persistence/repository/IDetallePedidoJpaRepository.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/persistence/adapter/PedidoRepositoryAdapter.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/persistence/mapper/IPedidoEntityMapper.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/endpoint/PedidoController.java` — `@PostMapping`
- `src/main/java/com/plazoleta/pedidos/infrastructure/endpoint/handler/GlobalExceptionHandler.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/config/BeanConfiguration.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/security/SecurityConfig.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/security/jwt/JwtTokenProvider.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/security/jwt/JwtAuthenticationFilter.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/config/RestTemplateConfig.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/usuario/UsuarioRestPedidosAdapter.java`
- `src/main/java/com/plazoleta/pedidos/infrastructure/restaurante/RestauranteRestPedidosAdapter.java`

### Test
- `src/test/java/com/plazoleta/pedidos/domain/usecase/CrearPedidoTest.java` — 6 tests

## Resultado de pruebas

```bash
./mvnw clean test
# Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
# BUILD SUCCESS
```

## Commits
1. `feat(common): agregar modelo pedido, puertos, SPI y adaptadores base`
2. `feat(realizar-pedido): implementar endpoint POST /pedidos con validaciones`
3. `test(realizar-pedido): agregar pruebas unitarias (6 escenarios)`
4. `docs: actualizar README con HU-11 realizar pedido`
