package exception.validation;

public class StudentFailAuthenticationException extends RuntimeException {
    public StudentFailAuthenticationException(String message) {
        super(message);
    }
}
