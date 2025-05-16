package codefusion.softwareproject1.service;

import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import java.util.List;

/**
 * Service interface for Quiz operations following Interface Segregation Principle.
 * Contains only methods relevant to Quiz entity operations.
 */
public interface QuizService {
    
    /**
     * Retrieves all quizzes.
     *
     * @return list of quiz DTOs
     */
    List<QuizDTO> getAllQuizzes();
    
    /**
     * Retrieves a quiz by its ID.
     *
     * @param id the quiz ID
     * @return the quiz DTO
     * @throws ResourceNotFoundException if the quiz is not found
     */
    QuizDTO getQuizById(Long id);
    
    /**
     * Creates a new quiz.
     *
     * @param quizDTO the quiz data transfer object
     * @return the created quiz DTO with ID field populated
     */
    QuizDTO createQuiz(QuizDTO quizDTO);
    
    /**
     * Updates an existing quiz.
     *
     * @param id the quiz ID
     * @param quizDTO the quiz data transfer object with updated fields
     * @return the updated quiz DTO
     * @throws ResourceNotFoundException if the quiz is not found
     */
    QuizDTO updateQuiz(Long id, QuizDTO quizDTO);
    
    /**
     * Deletes a quiz by its ID.
     *
     * @param id the quiz ID
     * @throws ResourceNotFoundException if the quiz is not found
     */
    void deleteQuiz(Long id);
    
    /**
     * Retrieves all published quizzes.
     *
     * @return list of published quiz DTOs
     */
    List<QuizDTO> getPublishedQuizzes();
    
    /**
     * Publishes a quiz.
     *
     * @param id the quiz ID
     * @return the updated quiz DTO
     * @throws ResourceNotFoundException if the quiz is not found
     */
    QuizDTO publishQuiz(Long id);
    
    /**
     * Unpublishes a quiz.
     *
     * @param id the quiz ID
     * @return the updated quiz DTO
     * @throws ResourceNotFoundException if the quiz is not found
     */
    QuizDTO unpublishQuiz(Long id);

    

    
} 