package com.plazoleta.pedidos.domain.spi;

import java.util.List;

public interface RestauranteValidacionPort {

    boolean existsById(Long idRestaurante);

    List<Long> validarPlatosPertenecenARestaurante(Long idRestaurante, List<Long> idsPlatos);

    List<Long> validarPlatosActivos(List<Long> idsPlatos);
}
