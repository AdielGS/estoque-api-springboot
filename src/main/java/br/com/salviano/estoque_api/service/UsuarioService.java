package br.com.salviano.estoque_api.service;

import br.com.salviano.estoque_api.controller.dto.DadosCadastroUsuario;
import br.com.salviano.estoque_api.controller.dto.DadosDetalhamentoUsuario;
import br.com.salviano.estoque_api.infra.exception.ResourceNotFoundException;
import br.com.salviano.estoque_api.model.Usuario;
import br.com.salviano.estoque_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public DadosDetalhamentoUsuario cadastrar(DadosCadastroUsuario dados) {
        if (usuarioRepository.findByLogin(dados.login()) != null) {
            throw new RuntimeException("Login já cadastrado!");
        }

        var senhaCriptografada = passwordEncoder.encode(dados.senha());

        Usuario novoUsuario = new Usuario();
        novoUsuario.setLogin(dados.login());
        novoUsuario.setEmail(dados.email());
        novoUsuario.setSenha(senhaCriptografada);
        novoUsuario.setRole(dados.role());
        novoUsuario.setAtivo(true);


        usuarioRepository.save(novoUsuario);

        return new DadosDetalhamentoUsuario(novoUsuario);
    }

    public List<DadosDetalhamentoUsuario> listarTodos() {
        var usuarios = usuarioRepository.findAll();

        return usuarios.stream()
                .map(DadosDetalhamentoUsuario::new)
                .collect(Collectors.toList());
    }

    public DadosDetalhamentoUsuario detalhar(Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado..."));

        return new DadosDetalhamentoUsuario(usuario);
    }

    public void desativar(Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }
}