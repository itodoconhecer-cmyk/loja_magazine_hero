package aulas.umc.oo.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record CarrinhoResponse(
        @NotNull UUID id,
        @NotNull UUID clienteId,
        boolean ativo,
        @NotNull @Valid List<ItemCarrinhoResponse> itens) {}
