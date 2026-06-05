package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.AsignarPedidoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.EmpleadoRestaurantePedidosPort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import java.util.Optional;

public class AsignarPedido implements AsignarPedidoPort {

    private final PedidoRepositoryPort pedidoRepository;
    private final EmpleadoRestaurantePedidosPort empleadoRestaurantePort;

    public AsignarPedido(PedidoRepositoryPort pedidoRepository,
                         EmpleadoRestaurantePedidosPort empleadoRestaurantePort) {
        this.pedidoRepository = pedidoRepository;
        this.empleadoRestaurantePort = empleadoRestaurantePort;
    }

    @Override
    public Pedido asignarPedido(Long idPedido, Long idEmpleado) {
        Optional<Long> idRestauranteOpt = empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(idEmpleado);
        if (idRestauranteOpt.isEmpty()) {
            throw new IllegalArgumentException("Empleado no asociado a ningun restaurante");
        }
        Long idRestaurante = idRestauranteOpt.get();

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe"));

        if (!pedido.getIdRestaurante().equals(idRestaurante)) {
            throw new IllegalArgumentException("No tienes permiso para asignarte a este pedido");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalArgumentException("Pedido asignado, se encuentra en estado PREPARACION");
        }

        if (pedido.getIdEmpleado() != null) {
            throw new IllegalArgumentException("El pedido ya tiene un empleado asignado");
        }

        pedido.setIdEmpleado(idEmpleado);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        return pedidoRepository.save(pedido);
    }
}