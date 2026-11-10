package aulas.umc.oo.strategy.processo;

import aulas.umc.oo.model.*;
import aulas.umc.oo.pipeline.*;
import aulas.umc.oo.repository.*;
import aulas.umc.oo.strategy.ProcessStrategy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ReservarEstoquePedidoStrategy implements ProcessStrategy {
    private final ProdutoRepository produtoRepo;
    private final MovimentacaoEstoqueRepository movRepo;

    public ReservarEstoquePedidoStrategy(ProdutoRepository p, MovimentacaoEstoqueRepository m) {
        produtoRepo = p;
        movRepo = m;
    }

    public Processo processo() { return Processo.APROVAR_PEDIDO; }
    public int ordem() { return 20; }
    public String nome() { return "reservar-estoque-atomico"; }

    @SuppressWarnings("unchecked")
    public void executar(ProcessContext c) {
        Pedido pedido = c.get("pedido", Pedido.class);
        List<ItemPedido> itens = (List<ItemPedido>) c.get("itensPedido", List.class);

        for (ItemPedido item : itens) {
            produtoRepo.retirarEstoqueSeDisponivel(item.produtoId, item.quantidade);

            MovimentacaoEstoque movimento = new MovimentacaoEstoque();
            movimento.produtoId = item.produtoId;
            movimento.tipo = TipoMovimentacao.SAIDA;
            movimento.quantidade = item.quantidade;
            movimento.motivo = "RESERVA_PEDIDO:" + pedido.id;
            movRepo.insert(movimento);
        }
    }
}
