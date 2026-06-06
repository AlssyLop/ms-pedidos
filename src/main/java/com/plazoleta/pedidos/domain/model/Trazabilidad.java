package com.plazoleta.pedidos.domain.model;

import java.time.LocalDateTime;

public class Trazabilidad {

    private String id;
    private Long idPedido;
    private String estadoAnterior;
    private String estadoNuevo;
    private LocalDateTime fechaCambio;
    private Long duracionEtapaMinutos;

    public Trazabilidad() {}

    public Trazabilidad(String id, Long idPedido, String estadoAnterior, String estadoNuevo,
                        LocalDateTime fechaCambio, Long duracionEtapaMinutos) {
        this.id = id;
        this.idPedido = idPedido;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaCambio = fechaCambio;
        this.duracionEtapaMinutos = duracionEtapaMinutos;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public String getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(String estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }

    public Long getDuracionEtapaMinutos() { return duracionEtapaMinutos; }
    public void setDuracionEtapaMinutos(Long duracionEtapaMinutos) { this.duracionEtapaMinutos = duracionEtapaMinutos; }
}