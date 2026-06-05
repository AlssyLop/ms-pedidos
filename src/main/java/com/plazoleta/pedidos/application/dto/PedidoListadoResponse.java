package com.plazoleta.pedidos.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoListadoResponse {

    private Long id;
    private Long idCliente;
    private String nombreCliente;
    private String celular;
    private Long idRestaurante;
    private String estado;
    private List<PlatoPedidoListadoResponse> platos;
    private LocalDateTime fechaCreacion;

    public PedidoListadoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public Long getIdRestaurante() { return idRestaurante; }
    public void setIdRestaurante(Long idRestaurante) { this.idRestaurante = idRestaurante; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<PlatoPedidoListadoResponse> getPlatos() { return platos; }
    public void setPlatos(List<PlatoPedidoListadoResponse> platos) { this.platos = platos; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}