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
public class ConcluirPedidoPagamentoStrategy implements ProcessStrategy {
 private final PedidoRepository repo; public ConcluirPedidoPagamentoStrategy(PedidoRepository r){repo=r;}
 public Processo processo(){return Processo.APROVACAO_PAGAMENTO;} public int ordem(){return 30;} public String nome(){return "aprovar-pedido-pago";}
 public void executar(ProcessContext c){ Pedido p=c.get("pedido",Pedido.class);p.status=StatusPedido.APROVADO;repo.update(p); }
}
