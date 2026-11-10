package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.Pagamento;
import aulas.umc.oo.model.StatusPagamento;
import aulas.umc.oo.pipeline.ProcessContext;
import aulas.umc.oo.pipeline.Processo;
import aulas.umc.oo.repository.PagamentoRepository;
import aulas.umc.oo.strategy.ProcessStrategy;
import org.springframework.stereotype.Component;

@Component
public class EstornarPagamentoStrategy implements ProcessStrategy {
    private final PagamentoRepository pagamentoRepository;

    public EstornarPagamentoStrategy(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public Processo processo() {
        return Processo.ESTORNAR_PAGAMENTO;
    }

    @Override
    public int ordem() {
        return 20;
    }

    @Override
    public String nome() {
        return "estornar-pagamento";
    }

    @Override
    public void executar(ProcessContext context) {
        Pagamento pagamento = context.get("pagamento", Pagamento.class);
        pagamento.status = StatusPagamento.ESTORNADO;
        pagamentoRepository.update(pagamento);
    }
}
