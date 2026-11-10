package aulas.umc.oo.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class MovimentacaoEstoque extends Domain {
    public UUID produtoId; public TipoMovimentacao tipo; public int quantidade; public String motivo; public LocalDateTime dataHora = LocalDateTime.now();
    public MovimentacaoEstoque() { this.id = UUID.randomUUID(); }
}
