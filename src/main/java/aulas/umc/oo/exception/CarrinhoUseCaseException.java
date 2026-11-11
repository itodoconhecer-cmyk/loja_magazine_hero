package aulas.umc.oo.exception;

public class CarrinhoUseCaseException extends UseCaseException {
    public CarrinhoUseCaseException(String codigo, String mensagem) {
        super("CARRINHO", codigo, mensagem);
    }
}
