package aulas.umc.oo.mapper;

import aulas.umc.oo.DTO.PagamentoResponse;
import aulas.umc.oo.model.Pagamento;
import org.springframework.stereotype.Component;

@Component
public class PagamentoMapper {
    public PagamentoResponse toResponse(Pagamento p) {
        return new PagamentoResponse(p.id, p.pedidoId, p.valor, p.status, p.meio);
    }
}
