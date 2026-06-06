package com.plazoleta.pedidos.application.dto;

import java.time.LocalDateTime;

public class PedidoEficienciaResponse {

    private Long idPedido;
    private Long idEmpleado;
    private String nombreEmpleado;
    private long tiempoTotalMinutos;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    public PedidoEficienciaResponse(Long idPedido, Long idEmpleado, String nombreEmpleado,
                                    long tiempoTotalMinutos, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.idPedido = idPedido;
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.tiempoTotalMinutos = tiempoTotalMinutos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public long getTiempoTotalMinutos() { return tiempoTotalMinutos; }
    public void setTiempoTotalMinutos(long tiempoTotalMinutos) { this.tiempoTotalMinutos = tiempoTotalMinutos; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
}
