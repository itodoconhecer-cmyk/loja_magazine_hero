package aulas.umc.oo.exception;

public class EstoqueUseCaseException extends UseCaseException {

    public EstoqueUseCaseException(ErrorCategory categoria, String codigo, String mensagem) {
        super("ESTOQUE", categoria, codigo, mensagem);
    }
}
