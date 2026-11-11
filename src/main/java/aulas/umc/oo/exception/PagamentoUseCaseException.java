package aulas.umc.oo.exception;

public class PagamentoUseCaseException extends UseCaseException {
    public PagamentoUseCaseException(String codigo, String mensagem) {
        super("PAGAMENTO", codigo, mensagem);
    }
}
