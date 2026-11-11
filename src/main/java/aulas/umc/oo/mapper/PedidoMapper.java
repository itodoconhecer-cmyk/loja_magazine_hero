package aulas.umc.oo.mapper;

import aulas.umc.oo.DTO.PedidoResponse;
import aulas.umc.oo.model.Pedido;
import org.springframework.stereotype.Component;

@Component
public class PedidoMapper {
    public PedidoResponse toResponse(Pedido p) {
        return new PedidoResponse(
                p.id, p.clienteId, p.carrinhoId, p.status, p.tipoFrete, p.subtotal, p.valorFrete, p.total, p.ativo);
    }
}
