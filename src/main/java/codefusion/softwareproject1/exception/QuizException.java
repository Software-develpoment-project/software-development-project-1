package codefusion.softwareproject1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * General exception for quiz-related errors.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST) // Or another appropriate status
public class QuizException extends RuntimeException {
    
    public QuizException(String message) {
        super(message);
    }
    
    public QuizException(String message, Throwable cause) {
        super(message, cause);
    }
}
