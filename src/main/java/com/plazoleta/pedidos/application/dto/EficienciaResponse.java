package com.plazoleta.pedidos.application.dto;

import java.util.List;

public class EficienciaResponse {

    private List<PedidoEficienciaResponse> pedidos;
    private List<RankingEmpleadoResponse> rankingEmpleados;

    public EficienciaResponse(List<PedidoEficienciaResponse> pedidos, List<RankingEmpleadoResponse> rankingEmpleados) {
        this.pedidos = pedidos;
        this.rankingEmpleados = rankingEmpleados;
    }

    public List<PedidoEficienciaResponse> getPedidos() { return pedidos; }
    public void setPedidos(List<PedidoEficienciaResponse> pedidos) { this.pedidos = pedidos; }

    public List<RankingEmpleadoResponse> getRankingEmpleados() { return rankingEmpleados; }
    public void setRankingEmpleados(List<RankingEmpleadoResponse> rankingEmpleados) { this.rankingEmpleados = rankingEmpleados; }
}
