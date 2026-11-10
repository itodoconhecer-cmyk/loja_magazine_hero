package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.MovimentacaoEstoque;
import aulas.umc.oo.model.Pedido;
import aulas.umc.oo.model.TipoMovimentacao;
import aulas.umc.oo.pipeline.ProcessContext;
import aulas.umc.oo.pipeline.Processo;
import aulas.umc.oo.repository.MovimentacaoEstoqueRepository;
import aulas.umc.oo.repository.PedidoRepository;
import aulas.umc.oo.repository.ProdutoRepository;
import aulas.umc.oo.strategy.ProcessStrategy;
import org.springframework.stereotype.Component;

@Component
public class DevolverEstoqueEstornoStrategy implements ProcessStrategy {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    public DevolverEstoqueEstornoStrategy(PedidoRepository pedidoRepository,
                                          ProdutoRepository produtoRepository,
                                          MovimentacaoEstoqueRepository movimentacaoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Override
    public Processo processo() {
        return Processo.ESTORNAR_PAGAMENTO;
    }

    @Override
    public int ordem() {
        return 30;
    }

    @Override
    public String nome() {
        return "devolver-estoque-estorno";
    }

    @Override
    public void executar(ProcessContext context) {
        Pedido pedido = context.get("pedido", Pedido.class);
        for (var item : pedidoRepository.findItems(pedido.id)) {
            produtoRepository.findByIdIncludingInactive(item.produtoId)
                    .orElseThrow(() -> new IllegalArgumentException("Produto do pedido nao encontrado"));
            produtoRepository.adicionarEstoque(item.produtoId, item.quantidade, false);

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.produtoId = item.produtoId;
            movimentacao.tipo = TipoMovimentacao.ENTRADA;
            movimentacao.quantidade = item.quantidade;
            movimentacao.motivo = "ESTORNO_PAGAMENTO_PEDIDO:" + pedido.id;
            movimentacaoRepository.insert(movimentacao);
        }
    }
}
