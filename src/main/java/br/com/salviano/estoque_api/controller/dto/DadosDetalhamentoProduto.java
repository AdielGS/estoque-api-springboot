package br.com.salviano.estoque_api.controller.dto;
import br.com.salviano.estoque_api.model.Produto;
import java.math.BigDecimal;

public record DadosDetalhamentoProduto(Long id, String nome, String descricao, BigDecimal preco, int quantidadeEmEstoque) {
    public DadosDetalhamentoProduto(Produto produto) {
        this(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.getQuantidadeEmEstoque());
    }
}