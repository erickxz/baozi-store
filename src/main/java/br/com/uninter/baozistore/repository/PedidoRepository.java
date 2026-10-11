package br.com.uninter.baozistore.repository;

import br.com.uninter.baozistore.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Acesso ao banco para Pedido.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Consultas derivadas do nome do método: o Spring gera o SQL sozinho.
    // Usadas para impedir a exclusão de cliente/produto que ainda tem pedidos.
    boolean existsByClienteId(Long clienteId);

    boolean existsByProdutoId(Long produtoId);
}
