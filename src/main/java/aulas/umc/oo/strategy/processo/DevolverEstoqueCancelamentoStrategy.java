package aulas.umc.oo.strategy.processo;
import aulas.umc.oo.pipeline.*; import aulas.umc.oo.strategy.ProcessStrategy; import aulas.umc.oo.repository.*; import aulas.umc.oo.model.*; import org.springframework.stereotype.Component;
@Component
public class DevolverEstoqueCancelamentoStrategy implements ProcessStrategy {
 private final PedidoRepository pedidoRepo; private final ProdutoRepository produtoRepo; private final MovimentacaoEstoqueRepository movRepo;
 public DevolverEstoqueCancelamentoStrategy(PedidoRepository p,ProdutoRepository pr,MovimentacaoEstoqueRepository m){pedidoRepo=p;produtoRepo=pr;movRepo=m;}
 public Processo processo(){return Processo.CANCELAR_PEDIDO;} public int ordem(){return 20;} public String nome(){return "devolver-estoque";}
 public void executar(ProcessContext c){Pedido p=c.get("pedido",Pedido.class);if(p.status==StatusPedido.AGUARDANDO_PAGAMENTO){for(var i:pedidoRepo.findItems(p.id)){produtoRepo.findByIdIncludingInactive(i.produtoId).orElseThrow(()->new IllegalArgumentException("Produto do pedido nao encontrado"));produtoRepo.adicionarEstoque(i.produtoId,i.quantidade,false);MovimentacaoEstoque m=new MovimentacaoEstoque();m.produtoId=i.produtoId;m.tipo=TipoMovimentacao.ENTRADA;m.quantidade=i.quantidade;m.motivo="CANCELAMENTO_PEDIDO:"+p.id;movRepo.insert(m);}}}
}
