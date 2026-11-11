package aulas.umc.oo.DTO;

import aulas.umc.oo.model.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.UUID;

public record MovimentacaoEstoqueResponse(
        @NotNull UUID id,
        @NotNull UUID produtoId,
        @NotNull TipoMovimentacao tipo,
        @Positive int quantidade,
        @NotBlank String motivo,
        @NotNull LocalDateTime dataHora) {}
