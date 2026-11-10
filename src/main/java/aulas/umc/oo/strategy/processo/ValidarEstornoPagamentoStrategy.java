package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.Pagamento;
import aulas.umc.oo.model.Pedido;
import aulas.umc.oo.model.StatusPagamento;
import aulas.umc.oo.model.StatusPedido;
import aulas.umc.oo.pipeline.ProcessContext;
import aulas.umc.oo.pipeline.Processo;
import aulas.umc.oo.repository.PagamentoRepository;
import aulas.umc.oo.repository.PedidoRepository;
import aulas.umc.oo.strategy.ProcessStrategy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidarEstornoPagamentoStrategy implements ProcessStrategy {
    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;

    public ValidarEstornoPagamentoStrategy(PagamentoRepository pagamentoRepository,
                                           PedidoRepository pedidoRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Processo processo() {
        return Processo.ESTORNAR_PAGAMENTO;
    }

    @Override
    public int ordem() {
        return 10;
    }

    @Override
    public String nome() {
        return "validar-estorno-pagamento";
    }

    @Override
    public void executar(ProcessContext context) {
        UUID pagamentoId = context.get("pagamentoId", UUID.class);
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento nao encontrado"));

        if (pagamento.status != StatusPagamento.APROVADO) {
            throw new IllegalStateException("Somente pagamento APROVADO pode ser estornado");
        }

        Pedido pedido = pedidoRepository.findById(pagamento.pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido do pagamento nao encontrado"));

        if (pedido.status != StatusPedido.APROVADO) {
            throw new IllegalStateException("Estorno exige pedido no status APROVADO");
        }

        context.put("pagamento", pagamento);
        context.put("pedido", pedido);
    }
}
