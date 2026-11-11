package aulas.umc.oo.DTO;

import aulas.umc.oo.model.*;
import java.math.BigDecimal;
import java.util.UUID;

public record PedidoResponse(
        UUID id,
        UUID clienteId,
        UUID carrinhoId,
        StatusPedido status,
        TipoFrete tipoFrete,
        BigDecimal subtotal,
        BigDecimal valorFrete,
        BigDecimal total,
        boolean ativo) {}
