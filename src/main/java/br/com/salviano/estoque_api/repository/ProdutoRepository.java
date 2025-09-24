package br.com.salviano.estoque_api.repository;

import br.com.salviano.estoque_api.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Produto> findById(Long id);

    Page<Produto> findAllByAtivoTrue(Pageable paginacao);

    Optional<Produto> findByNomeIgnoreCase(String nome);

    long countByAtivoTrue();
}


