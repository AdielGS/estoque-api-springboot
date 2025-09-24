package br.com.salviano.estoque_api.service;

import br.com.salviano.estoque_api.controller.dto.DadosDetalhamentoProduto;
import br.com.salviano.estoque_api.infra.exception.ResourceNotFoundException;
import br.com.salviano.estoque_api.model.Produto;
import br.com.salviano.estoque_api.repository.ProdutoRepository;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.com.salviano.estoque_api.controller.dto.DadosCadastroProduto;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public Produto cadastrar(DadosCadastroProduto dados) {
        // Procura se já existe um produto com o mesmo nome
        Optional<Produto> produtoExistenteOpt = produtoRepository.findByNomeIgnoreCase(dados.nome().trim());

        if (produtoExistenteOpt.isPresent()) {
            // Se existe, atualiza o estoque e outros dados
            Produto produtoExistente = produtoExistenteOpt.get();
            int novoEstoque = produtoExistente.getQuantidadeEmEstoque() + dados.quantidadeEmEstoque();
            produtoExistente.setQuantidadeEmEstoque(novoEstoque);
            produtoExistente.setPreco(dados.preco());
            produtoExistente.setDescricao(dados.descricao());
            produtoExistente.setAtivo(true); // Garante que ele fique/volte a ser ativo
            return produtoRepository.save(produtoExistente);
        } else {
            // Se não existe, cria um novo produto usando o nosso construtor seguro.
            // O "ativo = true" já está garantido dentro do construtor.
            Produto novoProduto = new Produto(dados);
            return produtoRepository.save(novoProduto);
        }
    }

    public Page<DadosDetalhamentoProduto> listar(Pageable paginacao) {
        return produtoRepository.findAllByAtivoTrue(paginacao)
                .map(DadosDetalhamentoProduto::new);
    }
    public Produto buscarPorId (Long id){
        Optional<Produto> produto = produtoRepository.findById(id);
        return produto.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado..."));
    }

    public Produto atualizar (Long id, Produto produtoAtualizado){
        Produto produtoExistente = buscarPorId(id);
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        produtoExistente.setQuantidadeEmEstoque(produtoAtualizado.getQuantidadeEmEstoque());
        return produtoRepository.save(produtoExistente);
    }

    @Transactional
    public void excluir(Long id) {
        var produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
        produto.setAtivo(false);
    }

}
