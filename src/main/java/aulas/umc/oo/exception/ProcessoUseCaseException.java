package aulas.umc.oo.exception;

public class ProcessoUseCaseException extends UseCaseException {

    public ProcessoUseCaseException(ErrorCategory categoria, String codigo, String mensagem) {
        super("PROCESSO", categoria, codigo, mensagem);
    }
}
