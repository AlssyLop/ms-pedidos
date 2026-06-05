package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.CrearPedidoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.ClienteValidacionPort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.RestauranteValidacionPort;
import com.plazoleta.pedidos.infrastructure.usuario.ClienteInfo;
import java.util.List;
import java.util.Optional;

public class CrearPedido implements CrearPedidoPort {

    private final PedidoRepositoryPort pedidoRepository;
    private final RestauranteValidacionPort restauranteValidacion;
    private final ClienteValidacionPort clienteValidacion;

    public CrearPedido(PedidoRepositoryPort pedidoRepository,
                       RestauranteValidacionPort restauranteValidacion,
                       ClienteValidacionPort clienteValidacion) {
        this.pedidoRepository = pedidoRepository;
        this.restauranteValidacion = restauranteValidacion;
        this.clienteValidacion = clienteValidacion;
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

        List<Long> platosValidos = restauranteValidacion.validarPlatosPertenecenARestaurante(
                pedido.getIdRestaurante(), idsPlatos);

        if (platosValidos.size() != idsPlatos.size()) {
            throw new IllegalArgumentException("El plato no pertenece al restaurante");
        }

        List<Long> platosActivos = restauranteValidacion.validarPlatosActivos(idsPlatos);
        if (platosActivos.size() != idsPlatos.size()) {
            throw new IllegalArgumentException("El plato no se encuentra disponible");
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
        return pedidoRepository.save(pedido);
    }
}