package com.plazoleta.pedidos.domain.model;

import java.util.List;

public class Eficiencia {

    private List<PedidoEficiencia> pedidos;
    private List<RankingEmpleado> rankingEmpleados;

    public Eficiencia(List<PedidoEficiencia> pedidos, List<RankingEmpleado> rankingEmpleados) {
        this.pedidos = pedidos;
        this.rankingEmpleados = rankingEmpleados;
    }

    public List<PedidoEficiencia> getPedidos() { return pedidos; }
    public void setPedidos(List<PedidoEficiencia> pedidos) { this.pedidos = pedidos; }

    public List<RankingEmpleado> getRankingEmpleados() { return rankingEmpleados; }
    public void setRankingEmpleados(List<RankingEmpleado> rankingEmpleados) { this.rankingEmpleados = rankingEmpleados; }
}
