package com.plazoleta.pedidos.application.dto;

public class EntregarPedidoRequest {
    private String pin;

    public EntregarPedidoRequest() {}
    public EntregarPedidoRequest(String pin) { this.pin = pin; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}