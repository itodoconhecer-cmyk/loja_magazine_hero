package aulas.umc.oo;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import aulas.umc.oo.exception.InitialFlowValidationException;
import aulas.umc.oo.pipeline.ProcessContext;
import aulas.umc.oo.pipeline.Processo;
import aulas.umc.oo.validation.FlowInitialValidator;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FlowInitialValidatorTest {

    private final FlowInitialValidator validator = new FlowInitialValidator();

    @Test
    void deveAceitarMovimentacaoValida() {
        var context = new ProcessContext()
                .put("produtoId", UUID.randomUUID())
                .put("quantidade", 1)
                .put("motivo", "AJUSTE");

        assertDoesNotThrow(() ->
                validator.validar(Processo.MOVIMENTACAO_ESTOQUE_SAIDA, context));
    }

    @Test
    void deveRejeitarQuantidadeInvalida() {
        var context = new ProcessContext()
                .put("produtoId", UUID.randomUUID())
                .put("quantidade", 0)
                .put("motivo", "AJUSTE");

        assertThrows(
                InitialFlowValidationException.class,
                () -> validator.validar(Processo.MOVIMENTACAO_ESTOQUE_SAIDA, context));
    }

    @Test
    void deveRejeitarChaveObrigatoriaAusente() {
        assertThrows(
                InitialFlowValidationException.class,
                () -> validator.validar(Processo.APROVAR_PEDIDO, new ProcessContext()));
    }
}
