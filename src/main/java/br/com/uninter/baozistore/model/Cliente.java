package br.com.uninter.baozistore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Entidade Cliente (tabela "cliente").
 * Um cliente pode fazer N pedidos (o vínculo fica na tabela "pedido").
 */
@Entity
@Table(name = "cliente")
public class Cliente {

    // Chave primária gerada automaticamente pelo banco (auto incremento)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    // Data em que o cliente foi cadastrado
    @Column(name = "cliente_desde", nullable = false)
    private LocalDate clienteDesde;

    // Construtor vazio exigido pelo JPA
    public Cliente() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getClienteDesde() {
        return clienteDesde;
    }

    public void setClienteDesde(LocalDate clienteDesde) {
        this.clienteDesde = clienteDesde;
    }
}
