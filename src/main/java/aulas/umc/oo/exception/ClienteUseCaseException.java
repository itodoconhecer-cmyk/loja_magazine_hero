package aulas.umc.oo.exception;

public class ClienteUseCaseException extends UseCaseException {
    public ClienteUseCaseException(String codigo, String mensagem) {
        super("CLIENTE", codigo, mensagem);
    }
}
