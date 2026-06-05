package com.plazoleta.pedidos.application.dto;

public class PlatoPedidoListadoResponse {

    private Long idPlato;
    private String nombre;
    private Integer cantidad;

    public PlatoPedidoListadoResponse() {}

    public PlatoPedidoListadoResponse(Long idPlato, String nombre, Integer cantidad) {
        this.idPlato = idPlato;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public Long getIdPlato() { return idPlato; }
    public void setIdPlato(Long idPlato) { this.idPlato = idPlato; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}