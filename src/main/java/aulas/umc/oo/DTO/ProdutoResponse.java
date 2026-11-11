package aulas.umc.oo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoResponse(
        @NotNull UUID id,
        @NotBlank String nome,
        @NotNull @PositiveOrZero BigDecimal preco,
        boolean ativo) {}
