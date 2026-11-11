package aulas.umc.oo.exception;

public class InitialFlowValidationException extends RuntimeException {
    private final String codigo;

    public InitialFlowValidationException(String codigo, String mensagem) {
        super(mensagem);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
