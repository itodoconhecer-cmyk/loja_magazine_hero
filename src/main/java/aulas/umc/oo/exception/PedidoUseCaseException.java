package aulas.umc.oo.exception;

public class PedidoUseCaseException extends UseCaseException {

    public PedidoUseCaseException(ErrorCategory categoria, String codigo, String mensagem) {
        super("PEDIDO", categoria, codigo, mensagem);
    }
}
