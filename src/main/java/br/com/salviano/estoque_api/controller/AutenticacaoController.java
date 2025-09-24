package br.com.salviano.estoque_api.controller;
import br.com.salviano.estoque_api.model.UserRole;
import br.com.salviano.estoque_api.infra.security.TokenService;
import br.com.salviano.estoque_api.model.Usuario;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

record DadosLogin(String login, String senha){}
record DadosTokenJWT(String token, UserRole role, String login) {}

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    private AuthenticationManager manager;
    private TokenService tokenService;

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosLogin dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var usuario = (Usuario) authentication.getPrincipal();
        var tokenJWT = tokenService.gerarToken(usuario);

        // Agora passamos o token E o perfil do utilizador na resposta
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT, usuario.getRole(), usuario.getLogin()));
    }
}
