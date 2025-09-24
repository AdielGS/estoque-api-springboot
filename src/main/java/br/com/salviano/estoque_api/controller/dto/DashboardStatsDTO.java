package br.com.salviano.estoque_api.controller.dto;

import java.math.BigDecimal;

public record DashboardStatsDTO(
        long totalProdutos,
        long totalClientes,
        long comandasAbertas,
        BigDecimal vendasHoje
) {
}