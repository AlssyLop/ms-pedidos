package com.plazoleta.pedidos.application.exception;

public class PedidoNoEncontradoException extends RuntimeException {
    public PedidoNoEncontradoException(String message) {
        super(message);
    }
}
