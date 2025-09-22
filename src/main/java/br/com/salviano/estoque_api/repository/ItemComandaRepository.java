package br.com.salviano.estoque_api.repository;

import br.com.salviano.estoque_api.model.ItemComanda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemComandaRepository extends JpaRepository<ItemComanda, Long> {
}