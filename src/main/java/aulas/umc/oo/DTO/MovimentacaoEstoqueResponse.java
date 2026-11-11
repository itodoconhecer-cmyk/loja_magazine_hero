package aulas.umc.oo.DTO;

import aulas.umc.oo.model.TipoMovimentacao;
import java.time.LocalDateTime;
import java.util.UUID;

public record MovimentacaoEstoqueResponse(
        UUID id, UUID produtoId, TipoMovimentacao tipo, int quantidade, String motivo, LocalDateTime dataHora) {}
