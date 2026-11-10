package aulas.umc.oo.DTO;

import jakarta.validation.constraints.*; import java.util.UUID;
public record MovimentacaoEstoqueRequest(@NotNull UUID produtoId, @Positive int quantidade, @NotBlank String motivo) {}
