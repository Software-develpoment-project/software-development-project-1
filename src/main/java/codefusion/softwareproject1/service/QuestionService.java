package codefusion.softwareproject1.service;

import codefusion.softwareproject1.dto.QuestionDTO;
import java.util.List;

/**
 * Service interface for Question operations following Interface Segregation Principle.
 * Contains only methods relevant to Question entity operations.
 */
public interface QuestionService {
    
    /**
     * Adds a new question to a quiz.
     *
     * @param questionDTO the question data transfer object
     * @return the created question DTO with ID field populated
     * @throws ResourceNotFoundException if the quiz is not found
     */
    QuestionDTO addQuestion(QuestionDTO questionDTO);
    
    /**
     * Retrieves all questions for a specific quiz.
     *
     * @param quizId the quiz ID
     * @return list of question DTOs
     * @throws ResourceNotFoundException if the quiz is not found
     */
    List<QuestionDTO> getQuestionsByQuizId(Long quizId);
    
    /**
     * Retrieves a question by its ID.
     *
     * @param id the question ID
     * @return the question DTO
     * @throws ResourceNotFoundException if the question is not found
     */
    QuestionDTO getQuestionById(Long id);
    
    /**
     * Deletes a question by its ID.
     *
     * @param id the question ID
     * @throws ResourceNotFoundException if the question is not found
     */
    void deleteQuestion(Long id);
} 