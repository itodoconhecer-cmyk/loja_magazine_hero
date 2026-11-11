package aulas.umc.oo.DTO;

import java.util.UUID;

/** Projecao calculada do estoque. Nao e um atributo persistido de Produto. */
public record SaldoEstoqueResponse(UUID produtoId, long saldo) {}
