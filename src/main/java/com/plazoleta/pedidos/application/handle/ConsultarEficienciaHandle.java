package com.plazoleta.pedidos.application.handle;

import com.plazoleta.pedidos.application.dto.EficienciaResponse;
import com.plazoleta.pedidos.application.dto.PedidoEficienciaResponse;
import com.plazoleta.pedidos.application.dto.RankingEmpleadoResponse;
import com.plazoleta.pedidos.domain.api.ConsultarEficienciaPort;
import com.plazoleta.pedidos.domain.model.Eficiencia;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarEficienciaHandle {

    private final ConsultarEficienciaPort consultarEficienciaPort;

    public ConsultarEficienciaHandle(ConsultarEficienciaPort consultarEficienciaPort) {
        this.consultarEficienciaPort = consultarEficienciaPort;
    }

    public EficienciaResponse consultar(Authentication authentication) {
        Long idPropietario = (Long) authentication.getPrincipal();
        Eficiencia eficiencia = consultarEficienciaPort.consultar(idPropietario);

        List<PedidoEficienciaResponse> pedidos = eficiencia.getPedidos().stream()
                .map(p -> new PedidoEficienciaResponse(
                        p.getIdPedido(),
                        p.getIdEmpleado(),
                        p.getNombreEmpleado(),
                        p.getTiempoTotalMinutos(),
                        p.getFechaInicio(),
                        p.getFechaFin()))
                .toList();

        List<RankingEmpleadoResponse> ranking = eficiencia.getRankingEmpleados().stream()
                .map(r -> new RankingEmpleadoResponse(
                        r.getIdEmpleado(),
                        r.getNombreEmpleado(),
                        r.getTiempoPromedioMinutos()))
                .toList();

        return new EficienciaResponse(pedidos, ranking);
    }
}
