package br.com.salviano.estoque_api.repository;

import br.com.salviano.estoque_api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository <Usuario, Long>{

    UserDetails findByLogin(String login);
}
