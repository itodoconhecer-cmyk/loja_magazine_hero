package aulas.umc.oo.DTO;

import jakarta.validation.constraints.NotNull; import java.util.UUID;
public record CarrinhoRequest(@NotNull UUID clienteId) {}
