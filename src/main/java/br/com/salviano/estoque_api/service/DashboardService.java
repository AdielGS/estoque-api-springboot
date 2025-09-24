
package br.com.salviano.estoque_api.service;

import br.com.salviano.estoque_api.controller.dto.DashboardStatsDTO;
import br.com.salviano.estoque_api.model.StatusComanda;
import br.com.salviano.estoque_api.repository.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class DashboardService {

    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;
    private final ComandaRepository comandaRepository;

    public DashboardService(ProdutoRepository p, ClienteRepository c, ComandaRepository co) {
        this.produtoRepository = p;
        this.clienteRepository = c;
        this.comandaRepository = co;
    }

    public DashboardStatsDTO getStats() {
        long totalProdutos = produtoRepository.countByAtivoTrue();
        long totalClientes = clienteRepository.countByAtivoTrue();
        long comandasAbertas = comandaRepository.countByStatus(StatusComanda.ABERTA);

        BigDecimal vendasHoje = Optional.ofNullable(comandaRepository.sumVendasDoDia(LocalDate.now()))
                .orElse(BigDecimal.ZERO);

        return new DashboardStatsDTO(totalProdutos, totalClientes, comandasAbertas, vendasHoje);
    }
}