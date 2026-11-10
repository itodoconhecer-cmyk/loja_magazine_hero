package aulas.umc.oo.strategy.processo;
import aulas.umc.oo.pipeline.*; import aulas.umc.oo.strategy.ProcessStrategy; import aulas.umc.oo.repository.*; import aulas.umc.oo.model.*; import org.springframework.stereotype.Component;
@Component
public class ExecutarMovimentacaoSaidaStrategy implements ProcessStrategy {
 private final ProdutoRepository produtoRepo; private final MovimentacaoEstoqueRepository movRepo; public ExecutarMovimentacaoSaidaStrategy(ProdutoRepository p,MovimentacaoEstoqueRepository m){produtoRepo=p;movRepo=m;}
 public Processo processo(){return Processo.MOVIMENTACAO_ESTOQUE_SAIDA;} public int ordem(){return 20;} public String nome(){return "executar-saida-estoque";}
 public void executar(ProcessContext c){Produto p=c.get("produto",Produto.class);int q=c.get("quantidade",Integer.class);produtoRepo.retirarEstoqueSeDisponivel(p.id,q);MovimentacaoEstoque m=new MovimentacaoEstoque();m.produtoId=p.id;m.tipo=TipoMovimentacao.SAIDA;m.quantidade=q;m.motivo=c.get("motivo",String.class);movRepo.insert(m);}
}
