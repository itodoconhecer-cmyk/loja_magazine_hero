package aulas.umc.oo.exception;

public class EstoqueUseCaseException extends UseCaseException {
    public EstoqueUseCaseException(String codigo, String mensagem) {
        super("ESTOQUE", codigo, mensagem);
    }
}
