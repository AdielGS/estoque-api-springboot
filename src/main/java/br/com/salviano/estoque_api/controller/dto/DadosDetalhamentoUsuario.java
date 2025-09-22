package br.com.salviano.estoque_api.controller.dto;

import br.com.salviano.estoque_api.model.UserRole;
import br.com.salviano.estoque_api.model.Usuario;

public record DadosDetalhamentoUsuario(
        Long id,
        String login,
        String email,
        UserRole role,
        boolean ativo
) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getLogin(), usuario.getEmail(), usuario.getRole(), usuario.isAtivo());
    }
}