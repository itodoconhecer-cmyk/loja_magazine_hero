package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.*;
import aulas.umc.oo.pipeline.*;
import aulas.umc.oo.repository.*;
import aulas.umc.oo.strategy.ProcessStrategy;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class EsvaziarCarrinhoStrategy implements ProcessStrategy {
    private final CarrinhoRepository repo;

    public EsvaziarCarrinhoStrategy(CarrinhoRepository r) {
        repo = r;
    }

    public Processo processo() {
        return Processo.ESVAZIAR_CARRINHO;
    }

    public int ordem() {
        return 20;
    }

    public String nome() {
        return "esvaziar-carrinho";
    }

    public void executar(ProcessContext c) {
        repo.clearItems(c.get("carrinhoId", UUID.class));
    }
}
