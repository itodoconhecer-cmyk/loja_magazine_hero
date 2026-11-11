package aulas.umc.oo.exception;

public abstract class UseCaseException extends RuntimeException {

    private final String useCase;
    private final String codigo;
    private final ErrorCategory categoria;

    protected UseCaseException(
            String useCase,
            ErrorCategory categoria,
            String codigo,
            String mensagem) {
        super(mensagem);
        this.useCase = useCase;
        this.categoria = categoria;
        this.codigo = codigo;
    }

    public String getUseCase() {
        return useCase;
    }

    public String getCodigo() {
        return codigo;
    }

    public ErrorCategory getCategoria() {
        return categoria;
    }
}
