package aulas.umc.oo.DTO;

import java.util.List;
import java.util.UUID;

public record CarrinhoResponse(UUID id, UUID clienteId, boolean ativo, List<ItemCarrinhoResponse> itens) {}
