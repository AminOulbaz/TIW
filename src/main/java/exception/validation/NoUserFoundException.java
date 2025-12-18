package exception.validation;

public class NoUserFoundException extends RuntimeException {
    public NoUserFoundException(String message) {
        super(message);
    }
}
