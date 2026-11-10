package aulas.umc.oo.DTO;

import java.util.UUID;
public record ItemCarrinhoResponse(UUID id, UUID produtoId, int quantidade) {}
