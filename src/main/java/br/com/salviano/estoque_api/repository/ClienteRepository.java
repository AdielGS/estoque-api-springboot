package br.com.salviano.estoque_api.repository;

import br.com.salviano.estoque_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByTelefone(String telefone);
    long countByAtivoTrue();
}