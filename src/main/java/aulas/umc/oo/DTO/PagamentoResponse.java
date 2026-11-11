package aulas.umc.oo.DTO;

import aulas.umc.oo.model.StatusPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record PagamentoResponse(
        @NotNull UUID id,
        @NotNull UUID pedidoId,
        @NotNull @Positive BigDecimal valor,
        @NotNull StatusPagamento status,
        @NotBlank String meio) {}
