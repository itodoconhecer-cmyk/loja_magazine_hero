package aulas.umc.oo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record MovimentacaoEstoqueRequest(
        @NotNull(message = "produtoId e obrigatorio") UUID produtoId,
        @Positive(message = "quantidade deve ser maior que zero") int quantidade,
        @NotBlank(message = "motivo e obrigatorio")
        @Size(max = 255, message = "motivo deve ter no maximo 255 caracteres")
        String motivo) {}
