package br.com.salviano.estoque_api.service;

import br.com.salviano.estoque_api.controller.dto.DadosAberturaComanda;
import br.com.salviano.estoque_api.controller.dto.DadosDetalhamentoComanda;
import br.com.salviano.estoque_api.model.Comanda;
import br.com.salviano.estoque_api.model.StatusComanda;
import br.com.salviano.estoque_api.repository.ComandaRepository;
import br.com.salviano.estoque_api.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.salviano.estoque_api.controller.dto.DadosAdicionarItem;
import br.com.salviano.estoque_api.infra.exception.ResourceNotFoundException;
import br.com.salviano.estoque_api.model.ItemComanda;

import java.math.BigDecimal;

import br.com.salviano.estoque_api.model.Cliente;
import br.com.salviano.estoque_api.repository.ClienteRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComandaService {

    private final ComandaRepository comandaRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public ComandaService(ComandaRepository comandaRepository, ProdutoRepository produtoRepository, ClienteRepository clienteRepository) {
        this.comandaRepository = comandaRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public DadosDetalhamentoComanda abrirComanda(DadosAberturaComanda dados) {
        Comanda novaComanda = new Comanda();

        if (dados.clienteId() != null) {
            // Se um ID de cliente foi informado, busca o cliente e associa à comanda
            var cliente = clienteRepository.findById(dados.clienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + dados.clienteId()));
            novaComanda.setCliente(cliente);
        } else if (dados.nomeClienteOcasional() != null && !dados.nomeClienteOcasional().isBlank()) {
            // Se não, usa o nome do cliente ocasional
            novaComanda.setNomeClienteOcasional(dados.nomeClienteOcasional());
        } else {
            throw new RuntimeException("É necessário informar o ID de um cliente cadastrado ou o nome de um cliente ocasional.");
        }

        novaComanda.setDataAbertura(LocalDateTime.now());
        novaComanda.setStatus(StatusComanda.ABERTA);

        comandaRepository.save(novaComanda);

        return new DadosDetalhamentoComanda(novaComanda);
    }

    @Transactional
    public DadosDetalhamentoComanda adicionarItem(Long comandaId, DadosAdicionarItem dadosItem) {
        var comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new ResourceNotFoundException("Comanda não encontrada com o ID: " + comandaId));

        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new RuntimeException("Não é possível adicionar itens a uma comanda que não está aberta.");
        }

        var produto = produtoRepository.findById(dadosItem.produtoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + dadosItem.produtoId()));

        if (produto.getQuantidadeEmEstoque() < dadosItem.quantidade()) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
        }

        int novaQuantidade = produto.getQuantidadeEmEstoque() - dadosItem.quantidade();
        produto.setQuantidadeEmEstoque(novaQuantidade);

        var novoItem = new ItemComanda();
        novoItem.setComanda(comanda);
        novoItem.setProduto(produto);
        novoItem.setQuantidade(dadosItem.quantidade());
        novoItem.setPrecoUnitarioMomento(produto.getPreco());

        comanda.getItens().add(novoItem);

        return new DadosDetalhamentoComanda(comanda);
    }

    @Transactional
    public DadosDetalhamentoComanda fecharComanda(Long comandaId) {
        var comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new ResourceNotFoundException("Comanda não encontrada com o ID: " + comandaId));

        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new RuntimeException("Apenas comandas com status ABERTA podem ser fechadas.");
        }

        var valorTotal = comanda.getItens().stream()
                .map(item -> item.getPrecoUnitarioMomento().multiply(new BigDecimal(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        comanda.setStatus(StatusComanda.FECHADA);
        comanda.setDataFechamento(LocalDateTime.now());
        comanda.setValorTotal(valorTotal);

        return new DadosDetalhamentoComanda(comanda);
    }

    public List<DadosDetalhamentoComanda> listar() {
        return comandaRepository.findAll().stream()
                .map(DadosDetalhamentoComanda::new)
                .collect(Collectors.toList());
    }

    public DadosDetalhamentoComanda detalhar(Long id) {
        var comanda = comandaRepository.findByIdCompleto(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comanda não encontrada com o ID: " + id));

        return new DadosDetalhamentoComanda(comanda);
    }

    @Transactional
    public DadosDetalhamentoComanda cancelarComanda(Long comandaId) {

        var comanda = comandaRepository.findById(comandaId)
                .orElseThrow(() -> new ResourceNotFoundException("Comanda não encontrada com o ID: " + comandaId));

        if (comanda.getStatus() != StatusComanda.ABERTA) {
            throw new RuntimeException("Apenas comandas com status ABERTA podem ser canceladas.");
        }

        for (var item : comanda.getItens()) {
            var produto = item.getProduto();
            int novaQuantidade = produto.getQuantidadeEmEstoque() + item.getQuantidade();
            produto.setQuantidadeEmEstoque(novaQuantidade);
        }

        comanda.setStatus(StatusComanda.CANCELADA);

        return new DadosDetalhamentoComanda(comanda);
    }

}