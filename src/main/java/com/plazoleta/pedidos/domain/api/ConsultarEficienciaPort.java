package com.plazoleta.pedidos.domain.api;

import com.plazoleta.pedidos.domain.model.Eficiencia;

public interface ConsultarEficienciaPort {
    Eficiencia consultar(Long idPropietario);
}
