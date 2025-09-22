package br.com.salviano.estoque_api.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DadosAdicionarItem(
        @NotNull
        Long produtoId,
        @NotNull
        @Positive
        Integer quantidade
) {
}