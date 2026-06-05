package com.plazoleta.pedidos.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PlatoPedidoRequest {

    @NotNull
    private Long idPlato;

    @NotNull
    @Min(1)
    private Integer cantidad;

    public PlatoPedidoRequest() {}

    public PlatoPedidoRequest(Long idPlato, Integer cantidad) {
        this.idPlato = idPlato;
        this.cantidad = cantidad;
    }

    public Long getIdPlato() { return idPlato; }
    public void setIdPlato(Long idPlato) { this.idPlato = idPlato; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
