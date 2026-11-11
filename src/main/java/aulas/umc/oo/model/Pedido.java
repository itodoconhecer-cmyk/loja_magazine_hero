package aulas.umc.oo.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Pedido extends Domain {
    public UUID clienteId;
    public UUID carrinhoId;
    public StatusPedido status = StatusPedido.RASCUNHO;
    public TipoFrete tipoFrete = TipoFrete.NORMAL;
    public BigDecimal subtotal = BigDecimal.ZERO;
    public BigDecimal valorFrete = BigDecimal.ZERO;
    public BigDecimal total = BigDecimal.ZERO;
    public boolean ativo = true;

    public Pedido() {
        this.id = UUID.randomUUID();
    }
}
