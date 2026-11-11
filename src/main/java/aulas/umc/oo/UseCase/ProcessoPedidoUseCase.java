package aulas.umc.oo.UseCase;

import aulas.umc.oo.DTO.PipelineResponse;
import aulas.umc.oo.exception.ProcessoUseCaseException;
import aulas.umc.oo.model.TipoFrete;
import aulas.umc.oo.pipeline.PipelineExecutor;
import aulas.umc.oo.pipeline.ProcessContext;
import aulas.umc.oo.pipeline.Processo;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ProcessoPedidoUseCase {

    private final PipelineExecutor pipeline;

    public ProcessoPedidoUseCase(PipelineExecutor pipeline) {
        this.pipeline = pipeline;
    }

    private PipelineResponse executar(Processo processo, ProcessContext context) {
        try {
            ProcessContext resultado = pipeline.executar(processo, context);
            return new PipelineResponse(
                    processo.name(),
                    resultado.tarefasExecutadas(),
                    resultado.snapshot());
        } catch (ProcessoUseCaseException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            throw new ProcessoUseCaseException(
                    "PRC-" + processo.name(),
                    "Falha no processo " + processo.name() + ": " + exception.getMessage());
        }
    }

    public PipelineResponse calcularFrete(UUID id) {
        return executar(
                Processo.CALCULAR_FRETE,
                new ProcessContext().put("pedidoId", id));
    }

    public PipelineResponse aprovarPedido(UUID id) {
        return executar(
                Processo.APROVAR_PEDIDO,
                new ProcessContext().put("pedidoId", id));
    }

    public PipelineResponse aprovarPagamento(UUID id) {
        return executar(
                Processo.APROVACAO_PAGAMENTO,
                new ProcessContext().put("pagamentoId", id));
    }

    public PipelineResponse estornarPagamento(UUID id) {
        return executar(
                Processo.ESTORNAR_PAGAMENTO,
                new ProcessContext().put("pagamentoId", id));
    }

    public PipelineResponse esvaziarCarrinho(UUID id) {
        return executar(
                Processo.ESVAZIAR_CARRINHO,
                new ProcessContext().put("carrinhoId", id));
    }

    public PipelineResponse cancelarPedido(UUID id) {
        return executar(
                Processo.CANCELAR_PEDIDO,
                new ProcessContext().put("pedidoId", id));
    }

    public PipelineResponse excluirPedido(UUID id) {
        return executar(
                Processo.EXCLUIR_PEDIDO,
                new ProcessContext().put("pedidoId", id));
    }

    public PipelineResponse editarPedido(UUID id, TipoFrete tipo) {
        return executar(
                Processo.EDITAR_PEDIDO,
                new ProcessContext()
                        .put("pedidoId", id)
                        .put("tipoFrete", tipo));
    }

    public PipelineResponse entrada(UUID produtoId, int quantidade, String motivo) {
        return executar(
                Processo.MOVIMENTACAO_ESTOQUE_ENTRADA,
                new ProcessContext()
                        .put("produtoId", produtoId)
                        .put("quantidade", quantidade)
                        .put("motivo", motivo));
    }

    public PipelineResponse saida(UUID produtoId, int quantidade, String motivo) {
        return executar(
                Processo.MOVIMENTACAO_ESTOQUE_SAIDA,
                new ProcessContext()
                        .put("produtoId", produtoId)
                        .put("quantidade", quantidade)
                        .put("motivo", motivo));
    }
}
