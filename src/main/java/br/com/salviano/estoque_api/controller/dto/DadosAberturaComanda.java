package br.com.salviano.estoque_api.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosAberturaComanda(
        Long clienteId,
        String nomeClienteOcasional
) {
}