package br.com.salviano.estoque_api.controller.dto;

import br.com.salviano.estoque_api.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroUsuario(
        @NotBlank
        String login,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String senha,
        @NotNull
        UserRole role
) {
}