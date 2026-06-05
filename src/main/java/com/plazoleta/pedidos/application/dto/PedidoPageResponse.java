package com.plazoleta.pedidos.application.dto;

import java.util.List;

public class PedidoPageResponse {

    private List<PedidoListadoResponse> contenido;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;

    public PedidoPageResponse() {}

    public PedidoPageResponse(List<PedidoListadoResponse> contenido, int paginaActual,
                              int totalPaginas, long totalElementos) {
        this.contenido = contenido;
        this.paginaActual = paginaActual;
        this.totalPaginas = totalPaginas;
        this.totalElementos = totalElementos;
    }

    public List<PedidoListadoResponse> getContenido() { return contenido; }
    public void setContenido(List<PedidoListadoResponse> contenido) { this.contenido = contenido; }

    public int getPaginaActual() { return paginaActual; }
    public void setPaginaActual(int paginaActual) { this.paginaActual = paginaActual; }

    public int getTotalPaginas() { return totalPaginas; }
    public void setTotalPaginas(int totalPaginas) { this.totalPaginas = totalPaginas; }

    public long getTotalElementos() { return totalElementos; }
    public void setTotalElementos(long totalElementos) { this.totalElementos = totalElementos; }
}