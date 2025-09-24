// Crie este novo arquivo
package br.com.salviano.estoque_api.controller.dto; // Verifique seu pacote

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record DadosCadastroProduto(
        @NotBlank
        String nome,
        String descricao,
        @NotNull
        @PositiveOrZero
        BigDecimal preco,
        @NotNull
        @PositiveOrZero
        Integer quantidadeEmEstoque,
        Boolean ativo
) {
}