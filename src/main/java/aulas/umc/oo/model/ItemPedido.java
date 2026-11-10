package aulas.umc.oo.model;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemPedido extends Domain {
    public UUID pedidoId; public UUID produtoId; public int quantidade; public BigDecimal precoUnitario;
    public ItemPedido() { this.id = UUID.randomUUID(); }
}
