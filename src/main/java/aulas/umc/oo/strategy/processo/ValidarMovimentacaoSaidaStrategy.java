package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.Produto;
import aulas.umc.oo.pipeline.ProcessContext;
import aulas.umc.oo.pipeline.Processo;
import aulas.umc.oo.repository.MovimentacaoEstoqueRepository;
import aulas.umc.oo.repository.ProdutoRepository;
import aulas.umc.oo.strategy.ProcessStrategy;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidarMovimentacaoSaidaStrategy implements ProcessStrategy {

    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    public ValidarMovimentacaoSaidaStrategy(
            ProdutoRepository produtoRepository,
            MovimentacaoEstoqueRepository movimentacaoRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Override
    public Processo processo() {
        return Processo.MOVIMENTACAO_ESTOQUE_SAIDA;
    }

    @Override
    public int ordem() {
        return 10;
    }

    @Override
    public String nome() {
        return "validar-saldo-estoque";
    }

    @Override
    public void executar(ProcessContext context) {
        UUID produtoId = context.get("produtoId", UUID.class);
        int quantidade = context.get("quantidade", Integer.class);

        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto nao encontrado ou inativo"));

        // O lock do produto serializa as movimentacoes concorrentes.
        movimentacaoRepository.bloquearProduto(produtoId);

        long saldo = movimentacaoRepository.calcularSaldo(produtoId);
        if (saldo < quantidade) {
            throw new IllegalStateException(
                    "Estoque insuficiente. Saldo atual: " + saldo
            );
        }

        context.put("produto", produto);
        context.put("saldoAntes", saldo);
    }
}
