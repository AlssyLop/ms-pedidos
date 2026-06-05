package com.plazoleta.pedidos.domain.model.value;

public class ClienteInfo {

    private Long id;
    private String nombre;
    private String celular;

    public ClienteInfo() {}

    public ClienteInfo(Long id, String nombre, String celular) {
        this.id = id;
        this.nombre = nombre;
        this.celular = celular;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
}