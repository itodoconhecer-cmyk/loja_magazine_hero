package aulas.umc.oo.exception;

public class ProdutoUseCaseException extends UseCaseException {
    public ProdutoUseCaseException(String codigo, String mensagem) {
        super("PRODUTO", codigo, mensagem);
    }
}
