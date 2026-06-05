package com.plazoleta.pedidos.application.dto;

public class CancelarPedidoResponse {
    private String mensaje;

    public CancelarPedidoResponse() {}
    public CancelarPedidoResponse(String mensaje) { this.mensaje = mensaje; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}