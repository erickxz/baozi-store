package br.com.uninter.baozistore.repository;

import br.com.uninter.baozistore.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Acesso ao banco para Produto.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
