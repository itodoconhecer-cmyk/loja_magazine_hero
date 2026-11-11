package aulas.umc.oo.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record ItemCarrinhoRequest(
        @NotNull(message = "produtoId e obrigatorio") UUID produtoId,

        @Positive(message = "quantidade deve ser maior que zero")
        int quantidade) {}
