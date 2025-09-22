package br.com.salviano.estoque_api.controller.dto;

import br.com.salviano.estoque_api.model.Comanda;
import br.com.salviano.estoque_api.model.ItemComanda;
import br.com.salviano.estoque_api.model.StatusComanda;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record DadosDetalhamentoComanda(
        Long id,
        String nomeCliente,
        LocalDateTime dataAbertura,
        StatusComanda status,
        BigDecimal valorTotal,
        List<ItemComanda> itens
) {
    public DadosDetalhamentoComanda(Comanda comanda) {
        this(
                comanda.getId(),
                comanda.getCliente() != null ? comanda.getCliente().getNome() : comanda.getNomeClienteOcasional(),
                comanda.getDataAbertura(),
                comanda.getStatus(),
                comanda.getValorTotal(),
                comanda.getItens()
        );
    }
}