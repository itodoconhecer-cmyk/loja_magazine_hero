package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.pipeline.*;
import aulas.umc.oo.strategy.ProcessStrategy;
import aulas.umc.oo.repository.*;
import aulas.umc.oo.model.*;
import aulas.umc.oo.strategy.frete.FreteStrategyFactory;
import org.springframework.stereotype.Component;
import java.util.*;
import java.math.BigDecimal;

@Component
public class ValidarMovimentacaoSaidaStrategy implements ProcessStrategy {
 private final ProdutoRepository produtoRepo; public ValidarMovimentacaoSaidaStrategy(ProdutoRepository r){produtoRepo=r;}
 public Processo processo(){return Processo.MOVIMENTACAO_ESTOQUE_SAIDA;} public int ordem(){return 10;} public String nome(){return "validar-saida-estoque";}
 public void executar(ProcessContext c){ Produto p=produtoRepo.findById(c.get("produtoId",UUID.class)).orElseThrow();int q=c.get("quantidade",Integer.class);if(q<=0||p.estoque<q)throw new IllegalStateException("Quantidade invalida ou estoque insuficiente");c.put("produto",p); }
}
