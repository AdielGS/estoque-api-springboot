package br.com.salviano.estoque_api.controller.dto;

import br.com.salviano.estoque_api.model.Cliente;

public record DadosDetalhamentoCliente(
        Long id,
        String nome,
        String telefone,
        String email,
        boolean ativo
) {
    public DadosDetalhamentoCliente(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getTelefone(), cliente.getEmail(), cliente.isAtivo());
    }
}