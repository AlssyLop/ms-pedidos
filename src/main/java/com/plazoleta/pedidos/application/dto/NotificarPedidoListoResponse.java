package com.plazoleta.pedidos.application.dto;

public class NotificarPedidoListoResponse {

    private String mensaje;

    public NotificarPedidoListoResponse() {}

    public NotificarPedidoListoResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
