package aulas.umc.oo.strategy.processo;
import aulas.umc.oo.pipeline.*; import aulas.umc.oo.strategy.ProcessStrategy; import aulas.umc.oo.repository.*; import aulas.umc.oo.model.*; import org.springframework.stereotype.Component; import java.util.*;
@Component
public class ValidarPedidoParaFreteStrategy implements ProcessStrategy {
 private final PedidoRepository pedidoRepo; public ValidarPedidoParaFreteStrategy(PedidoRepository r){pedidoRepo=r;}
 public Processo processo(){return Processo.CALCULAR_FRETE;} public int ordem(){return 10;} public String nome(){return "validar-pedido-para-frete";}
 public void executar(ProcessContext c){Pedido p=pedidoRepo.findById(c.get("pedidoId",UUID.class)).orElseThrow(()->new IllegalArgumentException("Pedido nao encontrado"));if(p.status!=StatusPedido.RASCUNHO)throw new IllegalStateException("Frete so pode ser calculado para pedido RASCUNHO");c.put("pedido",p);}
}
