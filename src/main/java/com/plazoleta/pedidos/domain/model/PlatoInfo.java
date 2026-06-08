package com.plazoleta.pedidos.domain.model;

public class PlatoInfo {

    private Long idPlato;
    private String nombrePlato;
    private boolean activo;

    public PlatoInfo(Long idPlato, String nombrePlato, boolean activo) {
        this.idPlato = idPlato;
        this.nombrePlato = nombrePlato;
        this.activo = activo;
    }

    public Long getIdPlato() {
        return idPlato;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public boolean isActivo() {
        return activo;
    }
}
