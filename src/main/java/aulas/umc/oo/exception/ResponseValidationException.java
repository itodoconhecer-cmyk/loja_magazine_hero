package aulas.umc.oo.exception;

import java.util.List;

public class ResponseValidationException extends RuntimeException {
    private final List<String> violacoes;

    public ResponseValidationException(List<String> violacoes) {
        super("A resposta gerada pela aplicacao violou o contrato da API");
        this.violacoes = List.copyOf(violacoes);
    }

    public List<String> getViolacoes() {
        return violacoes;
    }
}
