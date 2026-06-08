package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.CrearPedidoPort;
import com.plazoleta.pedidos.domain.model.DetallePedido;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.model.PlatoInfo;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.ClienteValidacionPort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.RestauranteValidacionPort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import com.plazoleta.pedidos.domain.model.value.ClienteInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CrearPedido implements CrearPedidoPort {

    private final PedidoRepositoryPort pedidoRepository;
    private final RestauranteValidacionPort restauranteValidacion;
    private final ClienteValidacionPort clienteValidacion;
    private final TrazabilidadRepositoryPort trazabilidadRepository;

    public CrearPedido(PedidoRepositoryPort pedidoRepository,
                       RestauranteValidacionPort restauranteValidacion,
                       ClienteValidacionPort clienteValidacion,
                       TrazabilidadRepositoryPort trazabilidadRepository) {
        this.pedidoRepository = pedidoRepository;
        this.restauranteValidacion = restauranteValidacion;
        this.clienteValidacion = clienteValidacion;
        this.trazabilidadRepository = trazabilidadRepository;
    }

    @Override
    public Pedido crearPedido(Pedido pedido) {
        Optional<ClienteInfo> clienteOpt = clienteValidacion.obtenerCliente(pedido.getIdCliente());
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("El cliente no existe");
        }

        ClienteInfo cliente = clienteOpt.get();

        if (!restauranteValidacion.existsById(pedido.getIdRestaurante())) {
            throw new IllegalArgumentException("El restaurante no existe");
        }

        List<Long> idsPlatos = pedido.getDetalles().stream()
                .map(d -> d.getIdPlato()).toList();

        List<PlatoInfo> platosInfo = restauranteValidacion.obtenerInfoPlatos(
                pedido.getIdRestaurante(), idsPlatos);

        if (platosInfo.size() != idsPlatos.size()) {
            Set<Long> encontrados = platosInfo.stream().map(PlatoInfo::getIdPlato).collect(Collectors.toSet());
            Long faltante = idsPlatos.stream().filter(id -> !encontrados.contains(id)).findFirst().orElse(null);
            throw new IllegalArgumentException("El plato " + faltante + " no pertenece al restaurante");
        }

        for (PlatoInfo plato : platosInfo) {
            if (!plato.isActivo()) {
                throw new IllegalArgumentException("El plato " + plato.getIdPlato() + " no se encuentra disponible");
            }
        }

        Map<Long, PlatoInfo> mapaPlatos = platosInfo.stream()
                .collect(Collectors.toMap(PlatoInfo::getIdPlato, p -> p));
        for (DetallePedido detalle : pedido.getDetalles()) {
            PlatoInfo info = mapaPlatos.get(detalle.getIdPlato());
            detalle.setNombrePlato(info.getNombrePlato());
        }

        boolean tienePedidoActivo = pedidoRepository.existsByIdClienteAndEstadoIn(
                pedido.getIdCliente(),
                List.of(EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION, EstadoPedido.LISTO));

        if (tienePedidoActivo) {
            throw new IllegalArgumentException("Tienes un pedido en proceso");
        }

        pedido.setNombreCliente(cliente.getNombre());
        pedido.setCelular(cliente.getCelular());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        Pedido guardado = pedidoRepository.save(pedido);

        trazabilidadRepository.save(new Trazabilidad(
                null,
                guardado.getId(),
                guardado.getIdCliente(),
                guardado.getIdRestaurante(),
                null,
                EstadoPedido.PENDIENTE.name(),
                LocalDateTime.now(),
                null,
                null
        ));

        return guardado;
    }
}