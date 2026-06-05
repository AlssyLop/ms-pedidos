package com.plazoleta.pedidos.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "detalle_pedido", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_pedido", "id_plato"})
})
public class EntidadDetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private EntidadPedido pedido;

    @Column(name = "id_plato", nullable = false)
    private Long idPlato;

    @Column(name = "nombre_plato", nullable = false, length = 100)
    private String nombrePlato;

    @Column(nullable = false)
    private Integer cantidad;

    public EntidadDetallePedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EntidadPedido getPedido() { return pedido; }
    public void setPedido(EntidadPedido pedido) { this.pedido = pedido; }

    public Long getIdPlato() { return idPlato; }
    public void setIdPlato(Long idPlato) { this.idPlato = idPlato; }

    public String getNombrePlato() { return nombrePlato; }
    public void setNombrePlato(String nombrePlato) { this.nombrePlato = nombrePlato; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
}
