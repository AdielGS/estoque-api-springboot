package br.com.salviano.estoque_api.repository;

import br.com.salviano.estoque_api.model.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import br.com.salviano.estoque_api.model.StatusComanda;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.Optional;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    @Query("SELECT c FROM Comanda c LEFT JOIN FETCH c.cliente LEFT JOIN FETCH c.itens WHERE c.id = :id")
    Optional<Comanda> findByIdCompleto(Long id);

    long countByStatus(StatusComanda status);

    @Query("SELECT SUM(c.valorTotal) FROM Comanda c WHERE c.status = 'FECHADA' AND c.dataFechamento >= :inicioDoDia")
    BigDecimal sumVendasDoDia(LocalDate inicioDoDia);

}