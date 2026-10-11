package br.com.uninter.baozistore.controller;

import br.com.uninter.baozistore.dto.PedidoRequest;
import br.com.uninter.baozistore.model.Cliente;
import br.com.uninter.baozistore.model.Pedido;
import br.com.uninter.baozistore.model.Produto;
import br.com.uninter.baozistore.repository.ClienteRepository;
import br.com.uninter.baozistore.repository.PedidoRepository;
import br.com.uninter.baozistore.repository.ProdutoRepository;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Endpoints REST de Pedido: /pedidos
 * Recebe um PedidoRequest (ids + quantidade) e devolve o Pedido completo,
 * com os objetos Cliente e Produto dentro do JSON.
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            ClienteRepository clienteRepository,
                            ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
    }

    // POST /pedidos -> cria um pedido (201 Created ou 400 Bad Request)
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PedidoRequest request) {
        return preencherESalvar(new Pedido(), request, HttpStatus.CREATED);
    }

    // GET /pedidos -> lista todos (200 OK)
    @GetMapping
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    // GET /pedidos/{id} -> busca por id (200 OK ou 404 Not Found)
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /pedidos/{id} -> atualiza (200 OK, 400 Bad Request ou 404 Not Found)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody PedidoRequest request) {
        Optional<Pedido> existente = pedidoRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return preencherESalvar(existente.get(), request, HttpStatus.OK);
    }

    // DELETE /pedidos/{id} -> remove (204 No Content ou 404 Not Found)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Valida o request, busca Cliente e Produto pelos IDs e salva o pedido.
     * Usado tanto no POST quanto no PUT. Em caso de erro, devolve 400 com {"erro": "..."}.
     */
    private ResponseEntity<?> preencherESalvar(Pedido pedido, PedidoRequest request, HttpStatus statusSucesso) {
        if (request.quantidade() == null || request.quantidade() <= 0) {
            return erro("Quantidade deve ser maior que zero");
        }

        // findById não aceita null, por isso o id nulo é tratado antes
        Optional<Cliente> cliente = request.clienteId() == null
                ? Optional.empty() : clienteRepository.findById(request.clienteId());
        if (cliente.isEmpty()) {
            return erro("Cliente não encontrado: " + request.clienteId());
        }

        Optional<Produto> produto = request.produtoId() == null
                ? Optional.empty() : produtoRepository.findById(request.produtoId());
        if (produto.isEmpty()) {
            return erro("Produto não encontrado: " + request.produtoId());
        }

        pedido.setCliente(cliente.get());
        pedido.setProduto(produto.get());
        pedido.setQuantidade(request.quantidade());

        Pedido salvo = pedidoRepository.save(pedido);
        return ResponseEntity.status(statusSucesso).body(salvo);
    }

    // Monta a resposta 400 Bad Request no formato {"erro": "mensagem"}
    private ResponseEntity<Map<String, String>> erro(String mensagem) {
        return ResponseEntity.badRequest().body(Map.of("erro", mensagem));
    }
}
