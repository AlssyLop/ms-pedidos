package com.plazoleta.pedidos.application.handle;

import com.plazoleta.pedidos.application.dto.CambioEstadoResponse;
import com.plazoleta.pedidos.application.dto.TrazabilidadResponse;
import com.plazoleta.pedidos.domain.api.ConsultarTrazabilidadPort;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarTrazabilidadHandle {

    private final ConsultarTrazabilidadPort consultarTrazabilidadPort;

    public ConsultarTrazabilidadHandle(ConsultarTrazabilidadPort consultarTrazabilidadPort) {
        this.consultarTrazabilidadPort = consultarTrazabilidadPort;
    }

    public TrazabilidadResponse consultar(Long idPedido, Authentication authentication) {
        Long idCliente = (Long) authentication.getPrincipal();
        List<Trazabilidad> trazabilidad = consultarTrazabilidadPort.consultar(idPedido, idCliente);

        List<CambioEstadoResponse> cambios = trazabilidad.stream()
                .map(t -> new CambioEstadoResponse(t.getEstadoAnterior(), t.getEstadoNuevo(), t.getFechaCambio()))
                .toList();

        return new TrazabilidadResponse(idPedido, cambios);
    }
}