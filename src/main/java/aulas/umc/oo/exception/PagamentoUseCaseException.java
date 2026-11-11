package aulas.umc.oo.exception;

public class PagamentoUseCaseException extends UseCaseException {

    public PagamentoUseCaseException(ErrorCategory categoria, String codigo, String mensagem) {
        super("PAGAMENTO", categoria, codigo, mensagem);
    }
}
