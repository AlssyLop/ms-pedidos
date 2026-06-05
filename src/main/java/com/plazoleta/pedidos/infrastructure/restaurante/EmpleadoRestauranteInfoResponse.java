package com.plazoleta.pedidos.infrastructure.restaurante;

public class EmpleadoRestauranteInfoResponse {

    private Long idEmpleado;
    private Long idRestaurante;
    private Long idCargo;

    public EmpleadoRestauranteInfoResponse() {}

    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }

    public Long getIdRestaurante() { return idRestaurante; }
    public void setIdRestaurante(Long idRestaurante) { this.idRestaurante = idRestaurante; }

    public Long getIdCargo() { return idCargo; }
    public void setIdCargo(Long idCargo) { this.idCargo = idCargo; }
}