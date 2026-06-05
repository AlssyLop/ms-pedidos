package com.plazoleta.pedidos.infrastructure.document;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trazabilidad")
public class EntidadTrazabilidad {

    @Id
    private String id;
    private Long idPedido;
    private String estadoAnterior;
    private String estadoNuevo;
    private LocalDateTime fechaCambio;
    private Long duracionEtapaMinutos;

    public EntidadTrazabilidad() {}

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
