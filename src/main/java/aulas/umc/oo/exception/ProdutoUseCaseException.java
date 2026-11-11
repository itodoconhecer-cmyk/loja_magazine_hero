package aulas.umc.oo.exception;

public class ProdutoUseCaseException extends UseCaseException {

    public ProdutoUseCaseException(ErrorCategory categoria, String codigo, String mensagem) {
        super("PRODUTO", categoria, codigo, mensagem);
    }
}
