package com.plazoleta.pedidos.domain.model;

public class DetallePedido {

    private Long id;
    private Long idPedido;
    private Long idPlato;
    private String nombrePlato;
    private Integer cantidad;

    public DetallePedido(Long id, Long idPedido, Long idPlato, String nombrePlato, Integer cantidad) {
        this.id = id;
        this.idPedido = idPedido;
        this.idPlato = idPlato;
        this.nombrePlato = nombrePlato;
        this.cantidad = cantidad;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public Long getIdPlato() { return idPlato; }
    public void setIdPlato(Long idPlato) { this.idPlato = idPlato; }

    public String getNombrePlato() { return nombrePlato; }
    public void setNombrePlato(String nombrePlato) { this.nombrePlato = nombrePlato; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
