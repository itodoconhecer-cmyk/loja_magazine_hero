package aulas.umc.oo.DTO;

import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record ProdutoRequest(@NotBlank String nome, @NotNull @PositiveOrZero BigDecimal preco, @PositiveOrZero int estoque) {}
