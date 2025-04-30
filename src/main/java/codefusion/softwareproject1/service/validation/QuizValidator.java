package codefusion.softwareproject1.service.validation;

import codefusion.softwareproject1.dto.QuizDTO;
import org.springframework.stereotype.Component;

/**
 * Validator for Quiz data following Single Responsibility Principle.
 * Centralizes all validation logic for quizzes.
 */
@Component
public class QuizValidator {

    /**
     * Validates a quiz DTO for creation operation.
     * 
     * @param quizDTO The quiz DTO to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateForCreate(QuizDTO quizDTO) {
        if (quizDTO == null) {
            throw new IllegalArgumentException("Quiz data cannot be null");
        }
        
        validateCommon(quizDTO);
    }
    
    /**
     * Validates a quiz DTO for update operation.
     * 
     * @param quizDTO The quiz DTO to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateForUpdate(QuizDTO quizDTO) {
        if (quizDTO == null) {
            throw new IllegalArgumentException("Quiz data cannot be null");
        }
        
        // For updates, we might want to ensure the ID is provided
        if (quizDTO.getId() == null) {
            throw new IllegalArgumentException("Quiz ID must be provided for updates");
        }
        
        validateCommon(quizDTO);
    }
    
    /**
     * Common validation logic for both create and update operations.
     * 
     * @param quizDTO The quiz DTO to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateCommon(QuizDTO quizDTO) {
        // Validate title
        if (quizDTO.getTitle() == null || quizDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Quiz title cannot be empty");
        }
        
        if (quizDTO.getTitle().length() < 3) {
            throw new IllegalArgumentException("Quiz title must be at least 3 characters long");
        }
        
        if (quizDTO.getTitle().length() > 100) {
            throw new IllegalArgumentException("Quiz title cannot exceed 100 characters");
        }
        
        // Validate description (if provided)
        if (quizDTO.getDescription() != null && quizDTO.getDescription().length() > 500) {
            throw new IllegalArgumentException("Quiz description cannot exceed 500 characters");
        }
    }
} 