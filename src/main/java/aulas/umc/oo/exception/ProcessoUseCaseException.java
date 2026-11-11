package aulas.umc.oo.exception;

public class ProcessoUseCaseException extends UseCaseException {
    public ProcessoUseCaseException(String codigo, String mensagem) {
        super("PROCESSO", codigo, mensagem);
    }
}
