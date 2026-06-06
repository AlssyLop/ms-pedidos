package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.ConsultarEficienciaPort;
import com.plazoleta.pedidos.domain.model.Eficiencia;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.model.PedidoEficiencia;
import com.plazoleta.pedidos.domain.model.RankingEmpleado;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.PropietarioRestaurantePort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConsultarEficiencia implements ConsultarEficienciaPort {

    private final PedidoRepositoryPort pedidoRepository;
    private final PropietarioRestaurantePort propietarioRestaurantePort;
    private final TrazabilidadRepositoryPort trazabilidadRepository;

    public ConsultarEficiencia(PedidoRepositoryPort pedidoRepository,
                               PropietarioRestaurantePort propietarioRestaurantePort,
                               TrazabilidadRepositoryPort trazabilidadRepository) {
        this.pedidoRepository = pedidoRepository;
        this.propietarioRestaurantePort = propietarioRestaurantePort;
        this.trazabilidadRepository = trazabilidadRepository;
    }

    @Override
    public Eficiencia consultar(Long idPropietario) {
        Long idRestaurante = propietarioRestaurantePort.obtenerIdRestauranteDelPropietario(idPropietario)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro un restaurante para el propietario"));

        List<Pedido> pedidosEntregados = pedidoRepository.findAllByIdRestauranteAndEstado(idRestaurante, EstadoPedido.ENTREGADO);
        if (pedidosEntregados.isEmpty()) {
            throw new IllegalArgumentException("No hay pedidos entregados para calcular eficiencia");
        }

        List<Long> idsPedidos = pedidosEntregados.stream().map(Pedido::getId).toList();
        List<Trazabilidad> trazabilidad = trazabilidadRepository.findByIdPedidoInOrderByFechaCambioAsc(idsPedidos);

        Map<Long, List<Trazabilidad>> trazabilidadPorPedido = trazabilidad.stream()
                .collect(Collectors.groupingBy(Trazabilidad::getIdPedido));

        List<PedidoEficiencia> pedidosEficiencia = pedidosEntregados.stream()
                .map(p -> calcularEficienciaPedido(p, trazabilidadPorPedido.getOrDefault(p.getId(), List.of())))
                .toList();

        List<RankingEmpleado> ranking = calcularRanking(pedidosEficiencia);

        return new Eficiencia(pedidosEficiencia, ranking);
    }

    private PedidoEficiencia calcularEficienciaPedido(Pedido pedido, List<Trazabilidad> trazabilidadPedido) {
        long tiempoTotalMinutos;
        if (!trazabilidadPedido.isEmpty()) {
            tiempoTotalMinutos = trazabilidadPedido.stream()
                    .mapToLong(t -> t.getDuracionEtapaMinutos() != null ? t.getDuracionEtapaMinutos() : 0L)
                    .sum();
            if (tiempoTotalMinutos == 0) {
                tiempoTotalMinutos = ChronoUnit.MINUTES.between(
                        trazabilidadPedido.get(0).getFechaCambio(),
                        trazabilidadPedido.get(trazabilidadPedido.size() - 1).getFechaCambio());
            }
        } else {
            tiempoTotalMinutos = ChronoUnit.MINUTES.between(pedido.getFechaCreacion(), pedido.getFechaModificacion());
        }

        return new PedidoEficiencia(
                pedido.getId(),
                pedido.getIdEmpleado(),
                null, // nombreEmpleado se puede enriquecer luego si se desea
                tiempoTotalMinutos,
                pedido.getFechaCreacion(),
                pedido.getFechaModificacion()
        );
    }

    private List<RankingEmpleado> calcularRanking(List<PedidoEficiencia> pedidos) {
        Map<Long, List<PedidoEficiencia>> porEmpleado = pedidos.stream()
                .filter(p -> p.getIdEmpleado() != null)
                .collect(Collectors.groupingBy(PedidoEficiencia::getIdEmpleado));

        return porEmpleado.entrySet().stream()
                .map(entry -> {
                    long idEmpleado = entry.getKey();
                    List<PedidoEficiencia> lista = entry.getValue();
                    double promedio = lista.stream()
                            .mapToLong(PedidoEficiencia::getTiempoTotalMinutos)
                            .average()
                            .orElse(0.0);
                    return new RankingEmpleado(idEmpleado, null, promedio);
                })
                .sorted(Comparator.comparingDouble(RankingEmpleado::getTiempoPromedioMinutos))
                .toList();
    }
}
