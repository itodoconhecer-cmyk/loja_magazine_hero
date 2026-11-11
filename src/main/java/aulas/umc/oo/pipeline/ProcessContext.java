package aulas.umc.oo.pipeline;

import java.util.*;

public class ProcessContext {
    private final Map<String, Object> dados = new LinkedHashMap<>();
    private final List<String> executadas = new ArrayList<>();

    public ProcessContext put(String k, Object v) {
        dados.put(k, v);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String k, Class<T> t) {
        Object v = dados.get(k);
        if (v == null) throw new IllegalArgumentException("Dado ausente no contexto: " + k);
        return (T) v;
    }

    public Object get(String k) {
        return dados.get(k);
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
}
