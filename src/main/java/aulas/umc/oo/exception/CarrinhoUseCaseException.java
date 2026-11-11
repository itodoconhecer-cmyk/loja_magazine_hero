package aulas.umc.oo.exception;

public class CarrinhoUseCaseException extends UseCaseException {

    public CarrinhoUseCaseException(ErrorCategory categoria, String codigo, String mensagem) {
        super("CARRINHO", categoria, codigo, mensagem);
    }
}
