package br.com.salviano.estoque_api.service;

import br.com.salviano.estoque_api.controller.dto.DadosAtualizacaoCliente;
import br.com.salviano.estoque_api.controller.dto.DadosCadastroCliente;
import br.com.salviano.estoque_api.controller.dto.DadosDetalhamentoCliente;
import br.com.salviano.estoque_api.infra.exception.ResourceNotFoundException;
import br.com.salviano.estoque_api.model.Cliente;
import br.com.salviano.estoque_api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public DadosDetalhamentoCliente cadastrar(DadosCadastroCliente dados) {
        if (clienteRepository.findByTelefone(dados.telefone()).isPresent()) {
            throw new RuntimeException("Telefone já cadastrado para outro cliente.");
        }

        var cliente = new Cliente();
        cliente.setNome(dados.nome());
        cliente.setTelefone(dados.telefone());
        cliente.setEmail(dados.email());
        cliente.setAtivo(true);

        clienteRepository.save(cliente);

        return new DadosDetalhamentoCliente(cliente);
    }

    public List<DadosDetalhamentoCliente> listar() {
        return clienteRepository.findAll().stream()
                .map(DadosDetalhamentoCliente::new)
                .collect(Collectors.toList());
    }

    public DadosDetalhamentoCliente detalhar(Long id) {
        var cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + id));
        return new DadosDetalhamentoCliente(cliente);
    }

    public DadosDetalhamentoCliente atualizar(Long id, DadosAtualizacaoCliente dados) {
        var cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + id));

        if (dados.nome() != null) {
            cliente.setNome(dados.nome());
        }
        if (dados.telefone() != null) {

            if (clienteRepository.findByTelefone(dados.telefone()).filter(c -> !c.getId().equals(id)).isPresent()) {
                throw new RuntimeException("Telefone já cadastrado para outro cliente.");
            }
            cliente.setTelefone(dados.telefone());
        }
        if (dados.email() != null) {
            cliente.setEmail(dados.email());
        }

        clienteRepository.save(cliente);
        return new DadosDetalhamentoCliente(cliente);
    }

    public void desativar(Long id) {
        var cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com o ID: " + id));
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }
}