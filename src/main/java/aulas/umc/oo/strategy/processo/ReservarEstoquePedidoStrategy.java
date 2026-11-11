package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.ItemPedido;
import aulas.umc.oo.model.MovimentacaoEstoque;
import aulas.umc.oo.model.Pedido;
import aulas.umc.oo.model.TipoMovimentacao;
import aulas.umc.oo.pipeline.ProcessContext;
import aulas.umc.oo.pipeline.Processo;
import aulas.umc.oo.repository.MovimentacaoEstoqueRepository;
import aulas.umc.oo.strategy.ProcessStrategy;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReservarEstoquePedidoStrategy implements ProcessStrategy {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;

    public ReservarEstoquePedidoStrategy(MovimentacaoEstoqueRepository movimentacaoRepository) {
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Override
    public Processo processo() {
        return Processo.APROVAR_PEDIDO;
    }

    @Override
    public int ordem() {
        return 20;
    }

    @Override
    public String nome() {
        return "registrar-saida-estoque-pedido";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void executar(ProcessContext context) {
        Pedido pedido = context.get("pedido", Pedido.class);
        List<ItemPedido> itens = (List<ItemPedido>) context.get("itensPedido", List.class);

        // Ordem deterministica de locks reduz risco de deadlock entre pedidos
        // concorrentes contendo os mesmos produtos em ordens diferentes.
        List<ItemPedido> itensOrdenados = itens.stream()
                .sorted(Comparator.comparing(item -> item.produtoId.toString()))
                .toList();

        for (ItemPedido item : itensOrdenados) {
            movimentacaoRepository.bloquearProduto(item.produtoId);

            long saldo = movimentacaoRepository.calcularSaldo(item.produtoId);
            if (saldo < item.quantidade) {
                throw new IllegalStateException(
                        "Estoque insuficiente para o produto " + item.produtoId + ". Saldo atual: " + saldo);
            }

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
            movimentacao.produtoId = item.produtoId;
            movimentacao.tipo = TipoMovimentacao.SAIDA;
            movimentacao.quantidade = item.quantidade;
            movimentacao.motivo = "VENDA_PEDIDO:" + pedido.id;

            movimentacaoRepository.insert(movimentacao);
        }
    }
}
