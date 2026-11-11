package aulas.umc.oo.exception;

public abstract class UseCaseException extends RuntimeException {
    private final String useCase;
    private final String codigo;

    protected UseCaseException(String useCase, String codigo, String mensagem) {
        super(mensagem);
        this.useCase = useCase;
        this.codigo = codigo;
    }

    public String getUseCase() {
        return useCase;
    }

    public String getCodigo() {
        return codigo;
    }
}
