package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.*;
import aulas.umc.oo.model.*;
import aulas.umc.oo.pipeline.*;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class ProcessoPedidoUseCase {
    private final PipelineExecutor pipeline;

    public ProcessoPedidoUseCase(PipelineExecutor p) {
        pipeline = p;
    }

    private PipelineResponse run(Processo p, ProcessContext c) {
        ProcessContext r = pipeline.executar(p, c);
        return new PipelineResponse(p.name(), r.tarefasExecutadas(), r.snapshot());
    }

    public PipelineResponse calcularFrete(UUID id) {
        return run(Processo.CALCULAR_FRETE, new ProcessContext().put("pedidoId", id));
    }

    public PipelineResponse aprovarPedido(UUID id) {
        return run(Processo.APROVAR_PEDIDO, new ProcessContext().put("pedidoId", id));
    }

    public PipelineResponse aprovarPagamento(UUID id) {
        return run(Processo.APROVACAO_PAGAMENTO, new ProcessContext().put("pagamentoId", id));
    }

    public PipelineResponse estornarPagamento(UUID id) {
        return run(Processo.ESTORNAR_PAGAMENTO, new ProcessContext().put("pagamentoId", id));
    }

    public PipelineResponse esvaziarCarrinho(UUID id) {
        return run(Processo.ESVAZIAR_CARRINHO, new ProcessContext().put("carrinhoId", id));
    }

    public PipelineResponse cancelarPedido(UUID id) {
        return run(Processo.CANCELAR_PEDIDO, new ProcessContext().put("pedidoId", id));
    }

    public PipelineResponse excluirPedido(UUID id) {
        return run(Processo.EXCLUIR_PEDIDO, new ProcessContext().put("pedidoId", id));
    }

    public PipelineResponse editarPedido(UUID id, TipoFrete tipo) {
        return run(
                Processo.EDITAR_PEDIDO, new ProcessContext().put("pedidoId", id).put("tipoFrete", tipo));
    }

    public PipelineResponse entrada(UUID produtoId, int q, String motivo) {
        return run(
                Processo.MOVIMENTACAO_ESTOQUE_ENTRADA,
                new ProcessContext()
                        .put("produtoId", produtoId)
                        .put("quantidade", q)
                        .put("motivo", motivo));
    }

    public PipelineResponse saida(UUID produtoId, int q, String motivo) {
        return run(
                Processo.MOVIMENTACAO_ESTOQUE_SAIDA,
                new ProcessContext()
                        .put("produtoId", produtoId)
                        .put("quantidade", q)
                        .put("motivo", motivo));
    }
}
