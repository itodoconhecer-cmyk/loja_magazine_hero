package aulas.umc.oo.DTO;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record ItemCarrinhoRequest(
        @NotNull UUID produtoId, @Positive int quantidade) {}
