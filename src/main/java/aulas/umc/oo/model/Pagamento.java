package aulas.umc.oo.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Pagamento extends Domain {
    public UUID pedidoId; public BigDecimal valor; public StatusPagamento status = StatusPagamento.PENDENTE; public String meio;
    public Pagamento() { this.id = UUID.randomUUID(); }
}
