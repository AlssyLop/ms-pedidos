package com.plazoleta.pedidos.domain.model;

import java.time.LocalDateTime;

public class Trazabilidad {

    private String id;
    private Long idPedido;
    private Long idCliente;
    private Long idRestaurante;
    private String estadoAnterior;
    private String estadoNuevo;
    private LocalDateTime fechaCambio;
    private Long idEmpleado;
    private Long duracionEtapaMinutos;

    public Trazabilidad() {}

    public Trazabilidad(String id, Long idPedido, Long idCliente, Long idRestaurante,
                        String estadoAnterior, String estadoNuevo,
                        LocalDateTime fechaCambio, Long idEmpleado, Long duracionEtapaMinutos) {
        this.id = id;
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.idRestaurante = idRestaurante;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
        this.idEmpleado = idEmpleado;
        this.duracionEtapaMinutos = duracionEtapaMinutos;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public Long getIdCliente() { return idCliente; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public Long getIdRestaurante() { return idRestaurante; }
    public void setIdRestaurante(Long idRestaurante) { this.idRestaurante = idRestaurante; }

    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }

    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }

    public Long getDuracionEtapaMinutos() { return duracionEtapaMinutos; }
    public void setDuracionEtapaMinutos(Long duracionEtapaMinutos) { this.duracionEtapaMinutos = duracionEtapaMinutos; }
}