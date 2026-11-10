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
public class MarcarPedidoCanceladoStrategy implements ProcessStrategy {
 private final PedidoRepository repo; public MarcarPedidoCanceladoStrategy(PedidoRepository r){repo=r;}
 public Processo processo(){return Processo.CANCELAR_PEDIDO;} public int ordem(){return 30;} public String nome(){return "marcar-pedido-cancelado";}
 public void executar(ProcessContext c){ Pedido p=c.get("pedido",Pedido.class);p.status=StatusPedido.CANCELADO;repo.update(p); }
}
