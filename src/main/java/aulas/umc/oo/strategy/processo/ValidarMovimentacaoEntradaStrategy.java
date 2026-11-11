package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.*;
import aulas.umc.oo.pipeline.*;
import aulas.umc.oo.repository.*;
import aulas.umc.oo.strategy.ProcessStrategy;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class ValidarMovimentacaoEntradaStrategy implements ProcessStrategy {
    private final ProdutoRepository produtoRepo;

    public ValidarMovimentacaoEntradaStrategy(ProdutoRepository r) {
        produtoRepo = r;
    }

    public Processo processo() {
        return Processo.MOVIMENTACAO_ESTOQUE_ENTRADA;
    }

    public int ordem() {
        return 10;
    }

    public String nome() {
        return "validar-entrada-estoque";
    }

    public void executar(ProcessContext c) {
        Produto p = produtoRepo.findById(c.get("produtoId", UUID.class)).orElseThrow();
        int q = c.get("quantidade", Integer.class);
        if (q <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        c.put("produto", p);
    }
}
