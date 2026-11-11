package aulas.umc.oo.exception;

public class PedidoUseCaseException extends UseCaseException {
    public PedidoUseCaseException(String codigo, String mensagem) {
        super("PEDIDO", codigo, mensagem);
    }
}
