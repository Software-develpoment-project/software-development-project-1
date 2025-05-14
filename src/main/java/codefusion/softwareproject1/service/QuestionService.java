package codefusion.softwareproject1.service;

import codefusion.softwareproject1.dto.QuestionDTO;

import java.util.List;

public interface QuestionService {
    
    /**
     * Add a new question to a quiz
     * 
     * @param questionDTO The question to add
     * @return The created question with ID
     */
    QuestionDTO addQuestion(QuestionDTO questionDTO);
    
    /**
     * Get all questions for a specific quiz
     * 
     * @param quizId The ID of the quiz
     * @return List of questions for the quiz
     */
    List<QuestionDTO> getQuestionsByQuizId(Long quizId);
    
    /**
     * Get a question by its ID
     * 
     * @param id The question ID
     * @return The question
     */
    QuestionDTO getQuestionById(Long id);
    
    /**
     * Delete a question
     * 
     * @param id The question ID to delete
     */
    void deleteQuestion(Long id);
    
    /**
     * Update an existing question
     * 
     * @param id The question ID to update
     * @param questionDTO The updated question data
     * @return The updated question
     */
    QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO);
}