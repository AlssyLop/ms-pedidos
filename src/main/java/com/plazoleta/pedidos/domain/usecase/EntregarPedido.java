package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.EntregarPedidoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.EmpleadoRestaurantePedidosPort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import java.util.Optional;

public class EntregarPedido implements EntregarPedidoPort {

    private final PedidoRepositoryPort pedidoRepository;
    private final EmpleadoRestaurantePedidosPort empleadoRestaurantePort;

    public EntregarPedido(PedidoRepositoryPort pedidoRepository,
                          EmpleadoRestaurantePedidosPort empleadoRestaurantePort) {
        this.pedidoRepository = pedidoRepository;
        this.empleadoRestaurantePort = empleadoRestaurantePort;
    }

    @Override
    public Pedido entregarPedido(Long idPedido, Long idEmpleado, String pin) {
        Optional<Long> idRestauranteOpt = empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(idEmpleado);
        if (idRestauranteOpt.isEmpty()) {
            throw new IllegalArgumentException("Empleado no asociado a ningun restaurante");
        }
        Long idRestaurante = idRestauranteOpt.get();

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe"));

        if (!pedido.getIdRestaurante().equals(idRestaurante)) {
            throw new IllegalArgumentException("No tienes permiso para entregar este pedido");
        }

        if (pedido.getEstado() != EstadoPedido.LISTO) {
            throw new IllegalArgumentException("El pedido no se encuentra en estado LISTO");
        }

        if (pedido.getPin() == null || !pedido.getPin().equals(pin)) {
            throw new IllegalArgumentException("El pin de seguridad es incorrecto");
        }

        pedido.setEstado(EstadoPedido.ENTREGADO);
        return pedidoRepository.save(pedido);
    }
}