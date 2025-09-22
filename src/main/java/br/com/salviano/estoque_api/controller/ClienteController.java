package br.com.salviano.estoque_api.controller;

import br.com.salviano.estoque_api.controller.dto.DadosAtualizacaoCliente;
import br.com.salviano.estoque_api.controller.dto.DadosCadastroCliente;
import br.com.salviano.estoque_api.controller.dto.DadosDetalhamentoCliente;
import br.com.salviano.estoque_api.service.ClienteService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> cadastrar(@RequestBody @Valid DadosCadastroCliente dados, UriComponentsBuilder uriBuilder) {
        var cliente = clienteService.cadastrar(dados);
        var uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.id()).toUri();
        return ResponseEntity.created(uri).body(cliente);
    }

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoCliente>> listar() {
        var lista = clienteService.listar();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoCliente> detalhar(@PathVariable Long id) {
        var cliente = clienteService.detalhar(id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoCliente> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoCliente dados) {
        var cliente = clienteService.atualizar(id, dados);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        clienteService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}