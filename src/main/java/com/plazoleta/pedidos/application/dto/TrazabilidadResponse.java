package com.plazoleta.pedidos.application.dto;

import java.util.List;

public class TrazabilidadResponse {

    private Long idPedido;
    private List<CambioEstadoResponse> cambios;

    public TrazabilidadResponse() {}

    public TrazabilidadResponse(Long idPedido, List<CambioEstadoResponse> cambios) {
        this.idPedido = idPedido;
        this.cambios = cambios;
    }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public List<CambioEstadoResponse> getCambios() { return cambios; }
    public void setCambios(List<CambioEstadoResponse> cambios) { this.cambios = cambios; }
}