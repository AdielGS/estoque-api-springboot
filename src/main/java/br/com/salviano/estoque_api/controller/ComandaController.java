package br.com.salviano.estoque_api.controller;

import br.com.salviano.estoque_api.controller.dto.DadosAberturaComanda;
import br.com.salviano.estoque_api.controller.dto.DadosAdicionarItem;
import br.com.salviano.estoque_api.controller.dto.DadosDetalhamentoComanda;
import br.com.salviano.estoque_api.service.ComandaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/comandas")
public class ComandaController {

    private final ComandaService comandaService;

    @Autowired
    public ComandaController(ComandaService comandaService) {
        this.comandaService = comandaService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoComanda> abrir(@RequestBody @Valid DadosAberturaComanda dados, UriComponentsBuilder uriBuilder) {
        var comanda = comandaService.abrirComanda(dados);
        var uri = uriBuilder.path("/comandas/{id}").buildAndExpand(comanda.id()).toUri();

        return ResponseEntity.created(uri).body(comanda);
    }

    @PostMapping("/{id}/itens")
    @Transactional
    public ResponseEntity<DadosDetalhamentoComanda> adicionarItem(@PathVariable Long id, @RequestBody @Valid DadosAdicionarItem dadosItem) {
        var comandaAtualizada = comandaService.adicionarItem(id, dadosItem);
        return ResponseEntity.ok(comandaAtualizada);
    }

    @PostMapping("/{id}/fechar")
    @Transactional
    public ResponseEntity<DadosDetalhamentoComanda> fechar(@PathVariable Long id) {
        var comandaFechada = comandaService.fecharComanda(id);
        return ResponseEntity.ok(comandaFechada);
    }

    @PostMapping("/{id}/cancelar")
    @Transactional
    public ResponseEntity<DadosDetalhamentoComanda> cancelar(@PathVariable Long id) {
        var comandaCancelada = comandaService.cancelarComanda(id);
        return ResponseEntity.ok(comandaCancelada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoComanda> detalhar(@PathVariable Long id) {
        var comanda = comandaService.detalhar(id);
        return ResponseEntity.ok(comanda);
    }

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoComanda>> listar() {
        var lista = comandaService.listar();
        return ResponseEntity.ok(lista);
    }

}