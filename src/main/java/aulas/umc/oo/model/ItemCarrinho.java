package aulas.umc.oo.model;

import java.util.UUID;

public class ItemCarrinho extends Domain {
    public UUID carrinhoId; public UUID produtoId; public int quantidade;
    public ItemCarrinho() { this.id = UUID.randomUUID(); }
    public ItemCarrinho(UUID carrinhoId, UUID produtoId, int quantidade) { this(); this.carrinhoId=carrinhoId; this.produtoId=produtoId; this.quantidade=quantidade; }
}
