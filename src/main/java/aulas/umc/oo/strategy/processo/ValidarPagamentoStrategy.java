package aulas.umc.oo.strategy.processo;
import aulas.umc.oo.pipeline.*; import aulas.umc.oo.strategy.ProcessStrategy; import aulas.umc.oo.repository.*; import aulas.umc.oo.model.*; import org.springframework.stereotype.Component; import java.util.*;
@Component
public class ValidarPagamentoStrategy implements ProcessStrategy {
 private final PagamentoRepository pagamentoRepo; private final PedidoRepository pedidoRepo; public ValidarPagamentoStrategy(PagamentoRepository a,PedidoRepository b){pagamentoRepo=a;pedidoRepo=b;}
 public Processo processo(){return Processo.APROVACAO_PAGAMENTO;} public int ordem(){return 10;} public String nome(){return "validar-pagamento";}
 public void executar(ProcessContext c){Pagamento pg=pagamentoRepo.findById(c.get("pagamentoId",UUID.class)).orElseThrow(()->new IllegalArgumentException("Pagamento nao encontrado"));if(pg.status!=StatusPagamento.PENDENTE)throw new IllegalStateException("Somente pagamento PENDENTE pode ser aprovado");Pedido p=pedidoRepo.findById(pg.pedidoId).orElseThrow(()->new IllegalArgumentException("Pedido nao encontrado"));if(p.status!=StatusPedido.AGUARDANDO_PAGAMENTO)throw new IllegalStateException("Pedido nao aguarda pagamento");if(pg.valor.compareTo(p.total)!=0)throw new IllegalStateException("Valor do pagamento diferente do total do pedido");c.put("pagamento",pg).put("pedido",p);}
}
