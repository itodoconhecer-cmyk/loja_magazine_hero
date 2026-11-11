package aulas.umc.oo.DTO;

import aulas.umc.oo.model.StatusPedido;
import aulas.umc.oo.model.TipoFrete;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

public record PedidoResponse(
        @NotNull UUID id,
        @NotNull UUID clienteId,
        @NotNull UUID carrinhoId,
        @NotNull StatusPedido status,
        @NotNull TipoFrete tipoFrete,
        @NotNull @PositiveOrZero BigDecimal subtotal,
        @NotNull @PositiveOrZero BigDecimal valorFrete,
        @NotNull @PositiveOrZero BigDecimal total,
        boolean ativo) {}
