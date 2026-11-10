package aulas.umc.oo.strategy.processo;
import aulas.umc.oo.pipeline.*; import aulas.umc.oo.strategy.ProcessStrategy; import aulas.umc.oo.repository.*; import aulas.umc.oo.model.*; import org.springframework.stereotype.Component; import java.util.*; import java.math.BigDecimal;
@Component
public class CalcularSubtotalStrategy implements ProcessStrategy {
 private final CarrinhoRepository carrinhoRepo; private final ProdutoRepository produtoRepo; private final PedidoRepository pedidoRepo;
 public CalcularSubtotalStrategy(CarrinhoRepository c,ProdutoRepository p,PedidoRepository pr){carrinhoRepo=c;produtoRepo=p;pedidoRepo=pr;}
 public Processo processo(){return Processo.CALCULAR_FRETE;} public int ordem(){return 20;} public String nome(){return "calcular-subtotal-e-snapshot";}
 public void executar(ProcessContext c){
   Pedido p=c.get("pedido",Pedido.class); var itens=carrinhoRepo.findItems(p.carrinhoId); if(itens.isEmpty())throw new IllegalStateException("Carrinho vazio");
   BigDecimal subtotal=BigDecimal.ZERO; List<ItemPedido> snapshot=new ArrayList<>();
   for(var i:itens){if(i.quantidade<=0)throw new IllegalStateException("Quantidade invalida no carrinho");Produto produto=produtoRepo.findById(i.produtoId).orElseThrow(()->new IllegalArgumentException("Produto nao encontrado ou inativo"));subtotal=subtotal.add(produto.preco.multiply(BigDecimal.valueOf(i.quantidade)));ItemPedido ip=new ItemPedido();ip.pedidoId=p.id;ip.produtoId=produto.id;ip.quantidade=i.quantidade;ip.precoUnitario=produto.preco;snapshot.add(ip);}
   pedidoRepo.replaceItems(p.id,snapshot);p.subtotal=subtotal;c.put("subtotal",subtotal).put("itensPedido",snapshot);
 }
}
