package aulas.umc.oo.DTO;

import aulas.umc.oo.model.TipoFrete; import jakarta.validation.constraints.NotNull;
public record PedidoEdicaoRequest(@NotNull TipoFrete tipoFrete) {}
