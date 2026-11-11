package aulas.umc.oo.validation;

import aulas.umc.oo.exception.InitialFlowValidationException;
import aulas.umc.oo.model.TipoFrete;
import aulas.umc.oo.pipeline.ProcessContext;
import aulas.umc.oo.pipeline.Processo;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class FlowInitialValidator {

    public void validar(Processo processo, ProcessContext context) {
        if (processo == null) {
            throw new InitialFlowValidationException("FLOW-001", "Processo nao informado");
        }
        if (context == null) {
            throw new InitialFlowValidationException("FLOW-002", "Contexto do processo nao informado");
        }

        switch (processo) {
            case CALCULAR_FRETE, APROVAR_PEDIDO, CANCELAR_PEDIDO, EXCLUIR_PEDIDO ->
                require(context, "pedidoId", UUID.class);
            case APROVACAO_PAGAMENTO, ESTORNAR_PAGAMENTO ->
                require(context, "pagamentoId", UUID.class);
            case ESVAZIAR_CARRINHO ->
                require(context, "carrinhoId", UUID.class);
            case EDITAR_PEDIDO -> {
                require(context, "pedidoId", UUID.class);
                require(context, "tipoFrete", TipoFrete.class);
            }
            case MOVIMENTACAO_ESTOQUE_ENTRADA, MOVIMENTACAO_ESTOQUE_SAIDA -> {
                require(context, "produtoId", UUID.class);
                Integer quantidade = require(context, "quantidade", Integer.class);
                String motivo = require(context, "motivo", String.class);
                if (quantidade <= 0) {
                    throw new InitialFlowValidationException("FLOW-003", "Quantidade deve ser maior que zero");
                }
                if (motivo.isBlank()) {
                    throw new InitialFlowValidationException("FLOW-004", "Motivo da movimentacao e obrigatorio");
                }
            }
        }
    }

    private <T> T require(ProcessContext context, String chave, Class<T> tipo) {
        Object valor = context.get(chave);
        if (valor == null) {
            throw new InitialFlowValidationException("FLOW-005", "Campo obrigatorio ausente no fluxo: " + chave);
        }
        if (!tipo.isInstance(valor)) {
            throw new InitialFlowValidationException(
                    "FLOW-006", "Tipo invalido no fluxo para " + chave + ": esperado " + tipo.getSimpleName());
        }
        return tipo.cast(valor);
    }
}
