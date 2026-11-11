package aulas.umc.oo.DTO;

import aulas.umc.oo.model.TipoFrete;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PedidoRequest(
        @NotNull(message = "clienteId e obrigatorio") UUID clienteId,
        @NotNull(message = "carrinhoId e obrigatorio") UUID carrinhoId,
        @NotNull(message = "tipoFrete e obrigatorio") TipoFrete tipoFrete) {}
