package com.plazoleta.pedidos.application.dto;

public class RankingEmpleadoResponse {

    private Long idEmpleado;
    private String nombreEmpleado;
    private double tiempoPromedioMinutos;

    public RankingEmpleadoResponse(Long idEmpleado, String nombreEmpleado, double tiempoPromedioMinutos) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.tiempoPromedioMinutos = tiempoPromedioMinutos;
    }

    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public double getTiempoPromedioMinutos() { return tiempoPromedioMinutos; }
    public void setTiempoPromedioMinutos(double tiempoPromedioMinutos) { this.tiempoPromedioMinutos = tiempoPromedioMinutos; }
}
