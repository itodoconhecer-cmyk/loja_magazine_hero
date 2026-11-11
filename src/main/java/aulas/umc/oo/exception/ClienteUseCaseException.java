package aulas.umc.oo.exception;

public class ClienteUseCaseException extends UseCaseException {

    public ClienteUseCaseException(ErrorCategory categoria, String codigo, String mensagem) {
        super("CLIENTE", categoria, codigo, mensagem);
    }
}
