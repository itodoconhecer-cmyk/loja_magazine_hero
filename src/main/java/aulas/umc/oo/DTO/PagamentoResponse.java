package aulas.umc.oo.DTO;

import aulas.umc.oo.model.StatusPagamento; import java.math.BigDecimal; import java.util.UUID;
public record PagamentoResponse(UUID id, UUID pedidoId, BigDecimal valor, StatusPagamento status, String meio) {}
