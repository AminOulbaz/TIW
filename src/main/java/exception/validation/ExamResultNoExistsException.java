package exception.validation;

public class ExamResultNoExistsException extends RuntimeException {
    public ExamResultNoExistsException(String message) {
        super(message);
    }
}
