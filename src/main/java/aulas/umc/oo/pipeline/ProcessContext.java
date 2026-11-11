package aulas.umc.oo.pipeline;

import java.math.BigDecimal;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProcessContext {

    private final Map<String, Object> dados = new LinkedHashMap<>();
    private final List<String> executadas = new ArrayList<>();

    public ProcessContext put(String chave, Object valor) {
        dados.put(chave, valor);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String chave, Class<T> tipo) {
        Object valor = dados.get(chave);
        if (valor == null) {
            throw new IllegalArgumentException("Dado ausente no contexto: " + chave);
        }
        return (T) valor;
    }

    public Object get(String chave) {
        return dados.get(chave);
    }

    public void registrar(String nome) {
        executadas.add(nome);
    }

    public List<String> tarefasExecutadas() {
        return List.copyOf(executadas);
    }

    public Map<String, Object> snapshot() {
        return Map.copyOf(dados);
    }

    public Map<String, Object> publicSnapshot() {
        Map<String, Object> publico = new LinkedHashMap<>();
        dados.forEach((chave, valor) -> {
            Object serializavel = toPublicValue(valor);
            if (serializavel != null) {
                publico.put(chave, serializavel);
            }
        });
        return Map.copyOf(publico);
    }

    private Object toPublicValue(Object valor) {
        if (valor == null) {
            return null;
        }

        if (valor instanceof String
                || valor instanceof Number
                || valor instanceof Boolean
                || valor instanceof UUID
                || valor instanceof BigDecimal
                || valor instanceof Enum<?>
                || valor instanceof TemporalAccessor) {
            return valor;
        }

        if (valor instanceof Collection<?> collection) {
            List<Object> itens = collection.stream()
                    .map(this::toPublicValue)
                    .filter(item -> item != null)
                    .toList();

            return itens.size() == collection.size() ? itens : null;
        }

        return null;
    }
}
