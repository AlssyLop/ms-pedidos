package com.plazoleta.pedidos.application.dto;

public class AsignarPedidoResponse {
    private String mensaje;

    public AsignarPedidoResponse() {}
    public AsignarPedidoResponse(String mensaje) { this.mensaje = mensaje; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}