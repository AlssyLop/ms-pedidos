package com.plazoleta.pedidos.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Pedido {

    private Long id;
    private Long idCliente;
    private String nombreCliente;
    private String celular;
    private Long idRestaurante;
    private EstadoPedido estado;
    private Long idEmpleado;
    private String pin;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private List<DetallePedido> detalles;

    public Pedido(Long id, Long idCliente, String nombreCliente, String celular,
                  Long idRestaurante, EstadoPedido estado, Long idEmpleado, String pin,
                  LocalDateTime fechaCreacion, LocalDateTime fechaModificacion) {
        this.id = id;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.celular = celular;
        this.idRestaurante = idRestaurante;
        this.estado = estado;
        this.idEmpleado = idEmpleado;
        this.pin = pin;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

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

    public EstadoPedido getEstado() { return estado; }
    public void setEstado(EstadoPedido estado) { this.estado = estado; }

    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaModificacion() { return fechaModificacion; }
    public void setFechaModificacion(LocalDateTime fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}
