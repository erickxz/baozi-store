package br.com.uninter.baozistore.dto;

/**
 * Dados recebidos no corpo do POST/PUT de /pedidos.
 * Exemplo: {"clienteId": 1, "produtoId": 1, "quantidade": 6}
 * O controller usa os IDs para buscar o Cliente e o Produto no banco.
 */
public record PedidoRequest(Long clienteId, Long produtoId, Integer quantidade) {
}
