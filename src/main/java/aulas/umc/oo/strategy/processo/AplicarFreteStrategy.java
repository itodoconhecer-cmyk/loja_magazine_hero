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
public class AplicarFreteStrategy implements ProcessStrategy {
 private final FreteStrategyFactory factory; private final PedidoRepository pedidoRepo; public AplicarFreteStrategy(FreteStrategyFactory f,PedidoRepository p){factory=f;pedidoRepo=p;}
 public Processo processo(){return Processo.CALCULAR_FRETE;} public int ordem(){return 30;} public String nome(){return "aplicar-strategy-frete";}
 public void executar(ProcessContext c){ Pedido p=c.get("pedido",Pedido.class); BigDecimal subtotal=c.get("subtotal",BigDecimal.class); p.valorFrete=factory.obter(p.tipoFrete).calcular(subtotal); p.total=subtotal.add(p.valorFrete); pedidoRepo.update(p); c.put("valorFrete",p.valorFrete).put("total",p.total); }
}
