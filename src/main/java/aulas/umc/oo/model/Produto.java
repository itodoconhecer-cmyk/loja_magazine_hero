package aulas.umc.oo.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Produto extends Domain {
    public String nome; public BigDecimal preco; public int estoque; public boolean ativo = true;
    public Produto() { this.id = UUID.randomUUID(); }
    public Produto(String nome, BigDecimal preco, int estoque) { this(); this.nome = nome; this.preco = preco; this.estoque = estoque; }
}
