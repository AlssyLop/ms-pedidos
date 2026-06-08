package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.AsignarPedidoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.model.Trazabilidad;
import com.plazoleta.pedidos.domain.spi.EmpleadoRestaurantePedidosPort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;
import com.plazoleta.pedidos.domain.spi.TrazabilidadRepositoryPort;
import java.time.LocalDateTime;
import java.util.Optional;

public class AsignarPedido implements AsignarPedidoPort {

    private final PedidoRepositoryPort pedidoRepository;
    private final EmpleadoRestaurantePedidosPort empleadoRestaurantePort;
    private final TrazabilidadRepositoryPort trazabilidadRepository;

    public AsignarPedido(PedidoRepositoryPort pedidoRepository,
                         EmpleadoRestaurantePedidosPort empleadoRestaurantePort,
                         TrazabilidadRepositoryPort trazabilidadRepository) {
        this.pedidoRepository = pedidoRepository;
        this.empleadoRestaurantePort = empleadoRestaurantePort;
        this.trazabilidadRepository = trazabilidadRepository;
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

        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new IllegalArgumentException("El pedido fue cancelado");
        }
        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            throw new IllegalArgumentException("El pedido ya fue entregado");
        }
        if (pedido.getEstado() == EstadoPedido.LISTO) {
            throw new IllegalArgumentException("El pedido ya esta listo");
        }
        if (pedido.getEstado() == EstadoPedido.EN_PREPARACION) {
            throw new IllegalArgumentException("El pedido ya esta en preparacion");
        }
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalArgumentException("El pedido no esta disponible para asignar");
        }

        if (pedido.getIdEmpleado() != null) {
            throw new IllegalArgumentException("El pedido ya tiene un empleado asignado");
        }

        pedido.setIdEmpleado(idEmpleado);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        Pedido guardado = pedidoRepository.save(pedido);

        trazabilidadRepository.save(new Trazabilidad(
                null,
                guardado.getId(),
                guardado.getIdCliente(),
                guardado.getIdRestaurante(),
                EstadoPedido.PENDIENTE.name(),
                EstadoPedido.EN_PREPARACION.name(),
                LocalDateTime.now(),
                idEmpleado,
                null
        ));

        return guardado;
    }
}