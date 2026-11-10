package aulas.umc.oo.DTO;

import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.util.UUID;
public record PagamentoRequest(@NotNull UUID pedidoId, @NotNull @Positive BigDecimal valor, @NotBlank String meio) {}
