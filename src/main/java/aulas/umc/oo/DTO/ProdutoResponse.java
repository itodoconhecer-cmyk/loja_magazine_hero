package aulas.umc.oo.DTO;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoResponse(UUID id, String nome, BigDecimal preco, boolean ativo) {}
