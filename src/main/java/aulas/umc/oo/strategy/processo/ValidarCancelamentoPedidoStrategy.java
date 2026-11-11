package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.*;
import aulas.umc.oo.pipeline.*;
import aulas.umc.oo.repository.*;
import aulas.umc.oo.strategy.ProcessStrategy;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class ValidarCancelamentoPedidoStrategy implements ProcessStrategy {
    private final PedidoRepository repo;

    public ValidarCancelamentoPedidoStrategy(PedidoRepository r) {
        repo = r;
    }

    public Processo processo() {
        return Processo.CANCELAR_PEDIDO;
    }

    public int ordem() {
        return 10;
    }

    public String nome() {
        return "validar-cancelamento";
    }

    public void executar(ProcessContext c) {
        Pedido p = repo.findById(c.get("pedidoId", UUID.class))
                .orElseThrow(() -> new IllegalArgumentException("Pedido nao encontrado"));
        if (p.status != StatusPedido.RASCUNHO && p.status != StatusPedido.AGUARDANDO_PAGAMENTO)
            throw new IllegalStateException(
                    "Somente pedido RASCUNHO ou AGUARDANDO_PAGAMENTO pode ser cancelado; pedido pago exige processo de estorno");
        c.put("pedido", p);
    }
}
