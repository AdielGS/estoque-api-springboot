package br.com.salviano.estoque_api; // Verifique se o pacote está correto

import br.com.salviano.estoque_api.model.UserRole;
import br.com.salviano.estoque_api.model.Usuario;
import br.com.salviano.estoque_api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe um usuário "admin" para não criar duplicado a cada reinicialização
        if (usuarioRepository.findByLogin("admin") == null) {
            System.out.println("Criando usuário 'admin' inicial...");

            // Criptografa a senha usando o PasswordEncoder
            String senhaCriptografada = passwordEncoder.encode("123456");

            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setEmail("admin@email.com");
            admin.setSenha(senhaCriptografada); // Salva a senha já criptografada
            admin.setAtivo(true);
            admin.setRole(UserRole.GERENTE);

            // Salva o usuário no banco através do repositório
            usuarioRepository.save(admin);

            System.out.println("Usuário 'admin' criado com sucesso!");
        }
    }
}