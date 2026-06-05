package com.plazoleta.pedidos.infrastructure.usuario;

public class ClienteInfoResponse {

    private Long id;
    private String nombre;
    private String celular;

    public ClienteInfoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
}