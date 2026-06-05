package com.plazoleta.pedidos.application.dto;

public class PedidoResponse {

    private String mensaje;

    public PedidoResponse() {}

    public PedidoResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
