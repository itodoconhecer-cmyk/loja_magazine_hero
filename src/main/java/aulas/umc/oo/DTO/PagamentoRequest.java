package aulas.umc.oo.DTO;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public record PagamentoRequest(
        @NotNull(message = "pedidoId e obrigatorio") UUID pedidoId,

        @NotNull(message = "valor e obrigatorio")
        @Positive(message = "valor deve ser maior que zero")
        @Digits(integer = 10, fraction = 2, message = "valor deve possuir no maximo 10 inteiros e 2 casas decimais")
        BigDecimal valor,

        @NotBlank(message = "meio e obrigatorio") @Size(max = 40, message = "meio deve ter no maximo 40 caracteres")
        String meio) {}
