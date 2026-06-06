package com.plazoleta.pedidos.infrastructure.restaurante;

public class RestauranteInfoResponse {

    private Long id;
    private String nombre;

    public RestauranteInfoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
