# Task Record — Fase 7: Refactor ms-pedidos + H11

## Rama
`feature/refactor-ms-pedidos`

## Resumen
- Refactor completo de ms-pedidos: paquete `ordering_service` → `com.plazoleta.pedidos`
- Implementación de H11: crear pedido (CLIENTE)

## Build
- Renombrado paquete `ordering_service` → `com.plazoleta.pedidos`
- Main class: `PedidosApplication` (renombrado de `OrderingServiceApplication`)
- jjwt 0.12.6 + MapStruct 1.6.3 agregados al pom.xml
- spring-boot-starter-webmvc + webmvc-test (Spring Boot 4 naming)
- mongo-init.js creado para colección `trazabilidad`

## Common (Infraestructura)

### JWT
- `JwtTokenProvider` — validación local con secreto compartido
- `JwtAuthenticationFilter` — extrae idUsuario y rol del token
- `SecurityConfig` — stateless, todas las rutas autenticadas excepto Swagger/OpenAPI

### Entities JPA
- `EntidadPedido` — mapea tabla `pedido` (id, id_cliente, nombre_cliente, celular, id_restaurante, estado, id_empleado, pin, fecha_creacion, fecha_modificacion)
- `EntidadDetallePedido` — mapea tabla `detalle_pedido` (unique id_pedido+id_plato)
- `EstadoPedidoEntity` — enum (PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO)

### MongoDB
- `EntidadTrazabilidad` — colección `trazabilidad` (idPedido, estadoAnterior, estadoNuevo, fechaCambio, duracionEtapaMinutos)
- `ITrazabilidadMongoRepository` — findByIdPedidoOrderByFechaCambioAsc

### Repos JPA
- `IPedidoJpaRepository` — existsByIdClienteAndEstadoIn, findByIdRestaurante (paginado, con/sin filtro estado)
- `IDetallePedidoJpaRepository` — findByPedidoId

### Mappers
- `IPedidoEntityMapper` — MapStruct, convierte Pedido ↔ EntidadPedido, DetallePedido ↔ EntidadDetallePedido

### Adapters
- `PedidoRepositoryAdapter` — implementación de PedidoRepositoryPort
- `RestauranteRestPedidosAdapter` — RestTemplate → ms-restaurantes (validar restaurante, platos)
- `UsuarioRestPedidosAdapter` — RestTemplate → ms-usuarios (obtener datos del cliente)

### SPI
- `PedidoRepositoryPort` — save, findById, existsByIdClienteAndEstadoIn, findByIdRestaurante (paginado)
- `RestauranteValidacionPort` — existsById, validarPlatosPertenecenARestaurante, validarPlatosActivos
- `ClienteValidacionPort` — obtenerCliente (para obtener nombre y celular)

### Modelos dominio
- `Pedido`, `DetallePedido`, `EstadoPedido`

## H11 — Crear pedido

### Domain
- `CrearPedidoPort` — interfaz en `dominio/api`
- `CrearPedido` — use case: valida cliente existe, restaurante existe, platos pertenecen y activos, sin pedido activo, crea con estado PENDIENTE

### Application
- `PedidoRequest` / `PlatoPedidoRequest` — DTOs request
- `PedidoResponse` — DTO response (`{ mensaje }`)
- `PedidoFactory` — validación secuencial campo por campo (idRestaurante requerido, platos no vacíos, cantidad > 0)
- `CrearPedidoHandle` — extrae idCliente del SecurityContext, llama al use case

### Infrastructure
- `PedidoController` — `POST /pedidos` rol CLIENTE
- `GlobalExceptionHandler` — ValidacionException (400 {campo:mensaje}), IllegalArgumentException (400 {mensaje}), PedidoNoEncontradoException (404 {mensaje})

### Tests
- `CrearPedidoTest` — 6 escenarios (ok, cliente no existe, restaurante no existe, plato no pertenece, pedido activo, plato inactivo)

## Tests
- `./mvnw clean test` → **7 tests, BUILD SUCCESS**

## Commits (4)
1. `build`: refactor paquete ordering_service → com.plazoleta.pedidos + jjwt + MapStruct
2. `feat(common)`: infraestructura JWT, entities JPA, repos, mappers, adapters, MongoDB
3. `feat(H11)`: endpoint POST /pedidos, use case, DTOs, factory, handle
4. `test(H11)`: 6 escenarios unitarios
5. `docs`: README.md inicial ms-pedidos