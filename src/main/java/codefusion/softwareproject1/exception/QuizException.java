package codefusion.softwareproject1.exception;

/**
 * General exception for quiz-related errors.
 */
public class QuizException extends RuntimeException {
    
    public QuizException(String message) {
        super(message);
    }
    
    public QuizException(String message, Throwable cause) {
        super(message, cause);
    }
}
