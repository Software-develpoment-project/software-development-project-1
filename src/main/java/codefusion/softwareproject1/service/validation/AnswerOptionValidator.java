package codefusion.softwareproject1.service.validation;

import codefusion.softwareproject1.entity.AnswerOption;
import codefusion.softwareproject1.repo.AnswerOptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Validator for answer options to ensure business rules.
 * Follows Single Responsibility Principle by isolating validation logic.
 */
@Component
public class AnswerOptionValidator {
    
    private static final int MAX_ANSWER_OPTIONS_PER_QUESTION = 4;
    
    private final AnswerOptionRepo answerOptionRepository;
    
    @Autowired
    public AnswerOptionValidator(AnswerOptionRepo answerOptionRepository) {
        this.answerOptionRepository = answerOptionRepository;
    }
    
    /**
     * Validates that a question doesn't exceed the maximum number of answer options.
     * 
     * @param questionId the question ID to validate
     * @throws ResponseStatusException if validation fails
     */
    public void validateMaxOptionsNotExceeded(Long questionId) {
        List<AnswerOption> existingOptions = answerOptionRepository.findByQuestionId(questionId);
        if (existingOptions.size() >= MAX_ANSWER_OPTIONS_PER_QUESTION) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Question already has the maximum number of answer options (" + MAX_ANSWER_OPTIONS_PER_QUESTION + ")");
        }
    }
    
    /**
     * Validates that the last correct answer isn't being deleted.
     * 
     * @param answerOptionToDelete the answer option being deleted
     * @throws ResponseStatusException if validation fails
     */
    public void validateNotDeletingLastCorrectAnswer(AnswerOption answerOptionToDelete) {
        if (answerOptionToDelete.getIsCorrect()) {
            List<AnswerOption> allOptions = answerOptionRepository.findByQuestionId(answerOptionToDelete.getQuestion().getId());
            
            long correctAnswersCount = allOptions.stream()
                    .filter(AnswerOption::getIsCorrect)
                    .count();
            
            if (correctAnswersCount <= 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Cannot delete the only correct answer for this question");
            }
        }
    }
} 