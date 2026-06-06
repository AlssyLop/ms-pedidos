package com.plazoleta.pedidos.domain.spi;

import java.util.Optional;

public interface PropietarioRestaurantePort {
    Optional<Long> obtenerIdRestauranteDelPropietario(Long idPropietario);
}
