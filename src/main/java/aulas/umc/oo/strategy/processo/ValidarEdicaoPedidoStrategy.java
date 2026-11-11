package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.*;
import aulas.umc.oo.pipeline.*;
import aulas.umc.oo.repository.*;
import aulas.umc.oo.strategy.ProcessStrategy;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class ValidarEdicaoPedidoStrategy implements ProcessStrategy {
    private final PedidoRepository repo;

    public ValidarEdicaoPedidoStrategy(PedidoRepository r) {
        repo = r;
    }

    public Processo processo() {
        return Processo.EDITAR_PEDIDO;
    }

    public int ordem() {
        return 10;
    }

    public String nome() {
        return "validar-edicao-pedido";
    }

    public void executar(ProcessContext c) {
        Pedido p = repo.findById(c.get("pedidoId", UUID.class)).orElseThrow();
        if (p.status != StatusPedido.RASCUNHO)
            throw new IllegalStateException("Somente pedido RASCUNHO pode ser editado");
        c.put("pedido", p);
    }
}
