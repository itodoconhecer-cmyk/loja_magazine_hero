package aulas.umc.oo.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;

public record SaldoEstoqueResponse(
        @NotNull UUID produtoId,
        @PositiveOrZero long saldo) {}
