package com.plazoleta.pedidos.application.dto;

import java.time.LocalDateTime;

public class CambioEstadoResponse {

    private String estadoAnterior;
    private String estadoNuevo;
    private LocalDateTime fechaCambio;

    public CambioEstadoResponse() {}

    public CambioEstadoResponse(String estadoAnterior, String estadoNuevo, LocalDateTime fechaCambio) {
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
    }

    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
}