package br.com.uninter.baozistore.repository;

import br.com.uninter.baozistore.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Acesso ao banco para Cliente. O JpaRepository já fornece
 * save, findAll, findById, existsById, deleteById etc.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
