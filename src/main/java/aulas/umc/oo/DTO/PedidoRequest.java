package aulas.umc.oo.DTO;

import aulas.umc.oo.model.TipoFrete; import jakarta.validation.constraints.NotNull; import java.util.UUID;
public record PedidoRequest(@NotNull UUID clienteId, @NotNull UUID carrinhoId, @NotNull TipoFrete tipoFrete) {}
