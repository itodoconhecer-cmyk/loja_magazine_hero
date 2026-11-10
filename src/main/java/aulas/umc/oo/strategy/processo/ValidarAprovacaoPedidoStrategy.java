package aulas.umc.oo.strategy.processo;
import aulas.umc.oo.pipeline.*; import aulas.umc.oo.strategy.ProcessStrategy; import aulas.umc.oo.repository.*; import aulas.umc.oo.model.*; import org.springframework.stereotype.Component; import java.util.*;
@Component
public class ValidarAprovacaoPedidoStrategy implements ProcessStrategy {
 private final PedidoRepository repo; public ValidarAprovacaoPedidoStrategy(PedidoRepository r){repo=r;}
 public Processo processo(){return Processo.APROVAR_PEDIDO;} public int ordem(){return 10;} public String nome(){return "validar-aprovacao-pedido";}
 public void executar(ProcessContext c){Pedido p=repo.findById(c.get("pedidoId",UUID.class)).orElseThrow(()->new IllegalArgumentException("Pedido nao encontrado"));if(p.status!=StatusPedido.RASCUNHO)throw new IllegalStateException("Somente pedido RASCUNHO pode ser aprovado");if(p.total.signum()<=0)throw new IllegalStateException("Calcule o frete antes de aprovar");var itens=repo.findItems(p.id);if(itens.isEmpty())throw new IllegalStateException("Pedido sem snapshot de itens; recalcule o frete");c.put("pedido",p).put("itensPedido",itens);}
}
