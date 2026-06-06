package com.plazoleta.pedidos.domain.usecase;

import com.plazoleta.pedidos.domain.api.NotificarPedidoListoPort;
import com.plazoleta.pedidos.domain.model.EstadoPedido;
import com.plazoleta.pedidos.domain.model.Pedido;
import com.plazoleta.pedidos.domain.spi.EmpleadoRestaurantePedidosPort;
import com.plazoleta.pedidos.domain.spi.NotificacionClientePort;
import com.plazoleta.pedidos.domain.spi.PedidoRepositoryPort;

import java.security.SecureRandom;

public class NotificarPedidoListo implements NotificarPedidoListoPort {

    private final PedidoRepositoryPort pedidoRepository;
    private final EmpleadoRestaurantePedidosPort empleadoRestaurantePort;
    private final NotificacionClientePort notificacionClientePort;

    public NotificarPedidoListo(PedidoRepositoryPort pedidoRepository,
                                 EmpleadoRestaurantePedidosPort empleadoRestaurantePort,
                                 NotificacionClientePort notificacionClientePort) {
        this.pedidoRepository = pedidoRepository;
        this.empleadoRestaurantePort = empleadoRestaurantePort;
        this.notificacionClientePort = notificacionClientePort;
    }

    @Override
    public String notificar(Long idPedido, Long idEmpleado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe"));

        if (pedido.getEstado() != EstadoPedido.EN_PREPARACION) {
            throw new IllegalArgumentException("El pedido no se encuentra en estado EN_PREPARACION");
        }

        Long idRestauranteEmpleado = empleadoRestaurantePort.obtenerIdRestauranteDelEmpleado(idEmpleado)
                .orElseThrow(() -> new IllegalArgumentException("El empleado no esta asociado a ningun restaurante"));

        if (!pedido.getIdRestaurante().equals(idRestauranteEmpleado)) {
            throw new IllegalArgumentException("No tienes permiso para modificar este pedido");
        }

        String pin = generarPin();
        pedido.setEstado(EstadoPedido.LISTO);
        pedido.setPin(pin);
        pedidoRepository.save(pedido);

        String mensaje = "Tu pedido esta listo. Usa el PIN " + pin + " para reclamarlo.";
        boolean notificacionExitosa = notificacionClientePort.enviarNotificacion(
                pedido.getId(), pedido.getCelular(), mensaje);

        if (notificacionExitosa) {
            return "Pedido marcado como LISTO y cliente notificado";
        } else {
            return "Pedido marcado como LISTO, pero no se pudo notificar al cliente";
        }
    }

    private String generarPin() {
        SecureRandom random = new SecureRandom();
        int pin = 100000 + random.nextInt(900000);
        return String.valueOf(pin);
    }
}
