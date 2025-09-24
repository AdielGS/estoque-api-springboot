package br.com.salviano.estoque_api.controller;

import br.com.salviano.estoque_api.controller.dto.DadosDetalhamentoProduto;
import br.com.salviano.estoque_api.model.Produto;
import br.com.salviano.estoque_api.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import java.util.List;
import jakarta.transaction.Transactional;
import br.com.salviano.estoque_api.controller.dto.DadosCadastroProduto;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    ProdutoController (ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Produto> cadastrar(@RequestBody @Valid DadosCadastroProduto dados, UriComponentsBuilder uriBuilder) {
        var produtoSalvo = produtoService.cadastrar(dados);
        var uri = uriBuilder.path("/api/produtos/{id}").buildAndExpand(produtoSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(produtoSalvo);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoProduto>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = produtoService.listar(paginacao);
        return ResponseEntity.ok(page);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<Produto> buscarPorId (@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<Produto> atualizar (@PathVariable Long id, @RequestBody Produto produto){
        Produto produtoAtualizado = produtoService.atualizar(id, produto);
        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/{id}")
    @Transactional // <-- Adicione esta anotação
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
