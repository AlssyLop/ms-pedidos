package com.plazoleta.pedidos.application.exception;

import java.util.List;

public class ErrorResponse {

    private String mensaje;
    private List<String> errores;

    public ErrorResponse() {}

    public ErrorResponse(String mensaje, List<String> errores) {
        this.mensaje = mensaje;
        this.errores = errores;
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public List<String> getErrores() { return errores; }
    public void setErrores(List<String> errores) { this.errores = errores; }
}
