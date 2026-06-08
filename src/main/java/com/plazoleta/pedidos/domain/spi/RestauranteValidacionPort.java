package com.plazoleta.pedidos.domain.spi;

import com.plazoleta.pedidos.domain.model.PlatoInfo;
import java.util.List;

public interface RestauranteValidacionPort {

    boolean existsById(Long idRestaurante);

    List<PlatoInfo> obtenerInfoPlatos(Long idRestaurante, List<Long> idsPlatos);
}
