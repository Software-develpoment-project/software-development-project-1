package codefusion.softwareproject1.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Extension of ErrorResponse that includes field-specific validation errors.
 * Follows Liskov Substitution Principle - can be used anywhere ErrorResponse is used.
 */
@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> fieldErrors;
    
    public ValidationErrorResponse(LocalDateTime timestamp, int status, String error, 
                                  String message, String path, Map<String, String> fieldErrors) {
        super(timestamp, status, error, message, path);
        this.fieldErrors = fieldErrors;
    }
} 