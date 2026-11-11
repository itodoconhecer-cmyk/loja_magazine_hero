package aulas.umc.oo.DTO;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProdutoRequest(
        @NotBlank(message = "nome e obrigatorio")
        @Size(min = 2, max = 160, message = "nome deve ter entre 2 e 160 caracteres")
        String nome,

        @NotNull(message = "preco e obrigatorio")
        @PositiveOrZero(message = "preco nao pode ser negativo")
        @Digits(integer = 10, fraction = 2, message = "preco deve possuir no maximo 10 inteiros e 2 casas decimais")
        BigDecimal preco) {}
