package aulas.umc.oo.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record ItemCarrinhoResponse(
        @NotNull UUID id, @NotNull UUID produtoId, @Positive int quantidade) {}
