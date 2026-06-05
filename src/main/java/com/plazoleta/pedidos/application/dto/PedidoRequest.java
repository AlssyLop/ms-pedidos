package com.plazoleta.pedidos.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PedidoRequest {

    @NotNull
    private Long idRestaurante;

    @NotEmpty
    @Valid
    private List<PlatoPedidoRequest> platos;

    public PedidoRequest() {}

    public PedidoRequest(Long idRestaurante, List<PlatoPedidoRequest> platos) {
        this.idRestaurante = idRestaurante;
        this.platos = platos;
    }

    public Long getIdRestaurante() { return idRestaurante; }
    public void setIdRestaurante(Long idRestaurante) { this.idRestaurante = idRestaurante; }

    public List<PlatoPedidoRequest> getPlatos() { return platos; }
    public void setPlatos(List<PlatoPedidoRequest> platos) { this.platos = platos; }
}
