package com.plazoleta.pedidos.domain.spi;

import java.util.Optional;

public interface EmpleadoRestaurantePedidosPort {
    Optional<Long> obtenerIdRestauranteDelEmpleado(Long idEmpleado);
}