package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.MovimentacaoEstoque;
import aulas.umc.oo.model.Produto;
import aulas.umc.oo.model.TipoMovimentacao;
import aulas.umc.oo.pipeline.ProcessContext;
import aulas.umc.oo.pipeline.Processo;
import aulas.umc.oo.repository.MovimentacaoEstoqueRepository;
import aulas.umc.oo.strategy.ProcessStrategy;
import org.springframework.stereotype.Component;

@Component
public class ExecutarMovimentacaoSaidaStrategy implements ProcessStrategy {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    public ExecutarMovimentacaoSaidaStrategy(
            MovimentacaoEstoqueRepository movimentacaoRepository
    ) {
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Override
    public Processo processo() {
        return Processo.MOVIMENTACAO_ESTOQUE_SAIDA;
    }

    @Override
    public int ordem() {
        return 20;
    }

    @Override
    public String nome() {
        return "registrar-saida-estoque";
    }

    @Override
    public void executar(ProcessContext context) {
        Produto produto = context.get("produto", Produto.class);
        int quantidade = context.get("quantidade", Integer.class);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.produtoId = produto.id;
        movimentacao.tipo = TipoMovimentacao.SAIDA;
        movimentacao.quantidade = quantidade;
        movimentacao.motivo = context.get("motivo", String.class);

        movimentacaoRepository.insert(movimentacao);

        context.put("saldoEstoque", movimentacaoRepository.calcularSaldo(produto.id));
    }
}
