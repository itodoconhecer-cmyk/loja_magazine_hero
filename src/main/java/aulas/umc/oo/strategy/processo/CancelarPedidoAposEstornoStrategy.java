package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.Pedido;
import aulas.umc.oo.model.StatusPedido;
import aulas.umc.oo.pipeline.ProcessContext;
import aulas.umc.oo.pipeline.Processo;
import aulas.umc.oo.repository.PedidoRepository;
import aulas.umc.oo.strategy.ProcessStrategy;
import org.springframework.stereotype.Component;

@Component
public class CancelarPedidoAposEstornoStrategy implements ProcessStrategy {
    private final PedidoRepository pedidoRepository;

    public CancelarPedidoAposEstornoStrategy(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Processo processo() {
        return Processo.ESTORNAR_PAGAMENTO;
    }

    @Override
    public int ordem() {
        return 40;
    }

    @Override
    public String nome() {
        return "cancelar-pedido-apos-estorno";
    }

    @Override
    public void executar(ProcessContext context) {
        Pedido pedido = context.get("pedido", Pedido.class);
        pedido.status = StatusPedido.CANCELADO;
        pedidoRepository.update(pedido);
    }
}
