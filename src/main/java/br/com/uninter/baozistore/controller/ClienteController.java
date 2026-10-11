package br.com.uninter.baozistore.controller;

import br.com.uninter.baozistore.model.Cliente;
import br.com.uninter.baozistore.repository.ClienteRepository;
import br.com.uninter.baozistore.repository.PedidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Endpoints REST de Cliente: /clientes
 * Todas as respostas são em JSON.
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    // Injeção de dependência por construtor (o Spring entrega os repositories prontos)
    public ClienteController(ClienteRepository clienteRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    // POST /clientes -> cria um cliente (201 Created)
    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
        cliente.setId(null); // garante que sempre será um novo registro
        if (cliente.getClienteDesde() == null) {
            cliente.setClienteDesde(LocalDate.now());
        }
        Cliente salvo = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // GET /clientes -> lista todos (200 OK)
    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    // GET /clientes/{id} -> busca por id (200 OK ou 404 Not Found)
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /clientes/{id} -> atualiza (200 OK ou 404 Not Found)
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente dados) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNome(dados.getNome());
                    // Se a data não for enviada, mantém a data original
                    if (dados.getClienteDesde() != null) {
                        cliente.setClienteDesde(dados.getClienteDesde());
                    }
                    return ResponseEntity.ok(clienteRepository.save(cliente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /clientes/{id} -> remove (204 No Content, 404 Not Found ou 409 Conflict)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Não deixa apagar cliente que ainda tem pedidos (violaria a chave estrangeira)
        if (pedidoRepository.existsByClienteId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Cliente possui pedidos vinculados e não pode ser excluído"));
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
