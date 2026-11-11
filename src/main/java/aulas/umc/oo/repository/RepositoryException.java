package aulas.umc.oo.repository;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String m, Throwable c) {
        super(m, c);
    }

    public RepositoryException(String m) {
        super(m);
    }
}
