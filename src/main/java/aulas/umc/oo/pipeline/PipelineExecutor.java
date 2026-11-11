package aulas.umc.oo.pipeline;

import aulas.umc.oo.exception.InitialFlowValidationException;
import aulas.umc.oo.strategy.ProcessStrategy;
import aulas.umc.oo.validation.FlowInitialValidator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PipelineExecutor {

    private static final Logger OPERATIONS_LOG = LoggerFactory.getLogger("OPERATIONS");

    private final List<ProcessStrategy> strategies;
    private final FlowInitialValidator initialValidator;

    public PipelineExecutor(List<ProcessStrategy> strategies, FlowInitialValidator initialValidator) {
        this.strategies = List.copyOf(strategies);
        this.initialValidator = initialValidator;
        validarConfiguracao();
    }

    private void validarConfiguracao() {
        Map<String, List<String>> conflitos = new LinkedHashMap<>();
        for (var strategy : strategies) {
            String chave = strategy.processo() + ":" + strategy.ordem();
            conflitos
                    .computeIfAbsent(chave, ignored -> new ArrayList<>())
                    .add(strategy.getClass().getSimpleName());
        }

        var duplicados = conflitos.entrySet().stream()
                .filter(entry -> entry.getValue().size() > 1)
                .toList();

        if (!duplicados.isEmpty()) {
            throw new IllegalStateException("Strategies com ordem duplicada: " + duplicados);
        }
    }

    @Transactional
    public ProcessContext executar(Processo processo, ProcessContext context) {
        initialValidator.validar(processo, context);

        var tarefas = strategies.stream()
                .filter(strategy -> strategy.processo() == processo)
                .sorted(Comparator.comparingInt(ProcessStrategy::ordem))
                .toList();

        if (tarefas.isEmpty()) {
            throw new InitialFlowValidationException(
                    "FLOW-007", "Pipeline sem strategies configuradas para " + processo);
        }

        OPERATIONS_LOG.info("process_started process={} strategies={}", processo, tarefas.size());

        for (var strategy : tarefas) {
            OPERATIONS_LOG.info(
                    "strategy_started process={} order={} strategy={}", processo, strategy.ordem(), strategy.nome());

            strategy.executar(context);
            context.registrar(strategy.nome());

            OPERATIONS_LOG.info(
                    "strategy_finished process={} order={} strategy={}", processo, strategy.ordem(), strategy.nome());
        }

        OPERATIONS_LOG.info("process_finished process={}", processo);
        return context;
    }
}
