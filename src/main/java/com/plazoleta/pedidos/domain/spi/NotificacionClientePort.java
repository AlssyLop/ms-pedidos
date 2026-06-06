package com.plazoleta.pedidos.domain.spi;

public interface NotificacionClientePort {
    boolean enviarNotificacion(Long idPedido, String celular, String mensaje);
}
