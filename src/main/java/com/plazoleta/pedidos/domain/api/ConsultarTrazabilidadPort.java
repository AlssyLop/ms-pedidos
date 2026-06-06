package com.plazoleta.pedidos.domain.api;

import com.plazoleta.pedidos.domain.model.Trazabilidad;
import java.util.List;

public interface ConsultarTrazabilidadPort {
    List<Trazabilidad> consultar(Long idPedido, Long idCliente);
}