package br.com.salviano.estoque_api.controller.dto;

import jakarta.validation.constraints.Email;

public record DadosAtualizacaoCliente(
        String nome,
        String telefone,
        @Email
        String email
) {
}