package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.*;
import aulas.umc.oo.pipeline.*;
import aulas.umc.oo.repository.*;
import aulas.umc.oo.strategy.ProcessStrategy;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class AprovarPagamentoStrategy implements ProcessStrategy {
    private final PagamentoRepository repo;

    public AprovarPagamentoStrategy(PagamentoRepository r) {
        repo = r;
    }

    public Processo processo() {
        return Processo.APROVACAO_PAGAMENTO;
    }

    public int ordem() {
        return 20;
    }

    public String nome() {
        return "aprovar-pagamento";
    }

    public void executar(ProcessContext c) {
        Pagamento pg = c.get("pagamento", Pagamento.class);
        pg.status = StatusPagamento.APROVADO;
        repo.update(pg);
    }
}
