package aulas.umc.oo.strategy.processo;
import aulas.umc.oo.pipeline.*; import aulas.umc.oo.strategy.ProcessStrategy; import aulas.umc.oo.repository.*; import aulas.umc.oo.model.*; import org.springframework.stereotype.Component;
@Component
public class ExecutarMovimentacaoEntradaStrategy implements ProcessStrategy {
 private final ProdutoRepository produtoRepo; private final MovimentacaoEstoqueRepository movRepo; public ExecutarMovimentacaoEntradaStrategy(ProdutoRepository p,MovimentacaoEstoqueRepository m){produtoRepo=p;movRepo=m;}
 public Processo processo(){return Processo.MOVIMENTACAO_ESTOQUE_ENTRADA;} public int ordem(){return 20;} public String nome(){return "executar-entrada-estoque";}
 public void executar(ProcessContext c){Produto p=c.get("produto",Produto.class);int q=c.get("quantidade",Integer.class);produtoRepo.adicionarEstoque(p.id,q,true);MovimentacaoEstoque m=new MovimentacaoEstoque();m.produtoId=p.id;m.tipo=TipoMovimentacao.ENTRADA;m.quantidade=q;m.motivo=c.get("motivo",String.class);movRepo.insert(m);}
}
