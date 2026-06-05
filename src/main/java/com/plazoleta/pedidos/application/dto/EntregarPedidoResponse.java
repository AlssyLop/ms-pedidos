package com.plazoleta.pedidos.application.dto;

public class EntregarPedidoResponse {
    private String mensaje;

    public EntregarPedidoResponse() {}
    public EntregarPedidoResponse(String mensaje) { this.mensaje = mensaje; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}