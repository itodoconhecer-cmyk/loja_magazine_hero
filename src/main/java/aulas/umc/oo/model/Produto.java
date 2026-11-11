package aulas.umc.oo.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Produto representa apenas os dados de catalogo.
 *
 * O saldo de estoque nao pertence ao Produto. Ele e calculado a partir
 * do livro-razão de MovimentacaoEstoque:
 * saldo = soma(ENTRADA) - soma(SAIDA).
 */
public class Produto extends Domain {
    public String nome;
    public BigDecimal preco;
    public boolean ativo = true;

    public Produto() {
        this.id = UUID.randomUUID();
    }

    public Produto(String nome, BigDecimal preco) {
        this();
        this.nome = nome;
        this.preco = preco;
    }
}
