package codefusion.softwareproject1.service;

import codefusion.softwareproject1.dto.AnswerOptionDTO;

import java.util.List;

public interface AnswerOptionService {
    
    /**
     * Add a new answer option to a question
     * 
     * @param answerOptionDTO The answer option to add
     * @return The created answer option with ID
     */
    AnswerOptionDTO addAnswerOption(AnswerOptionDTO answerOptionDTO);
    
    /**
     * Get all answer options for a specific question
     * 
     * @param questionId The ID of the question
     * @return List of answer options for the question
     */
    List<AnswerOptionDTO> getAnswerOptionsByQuestionId(Long questionId);
    
    /**
     * Get an answer option by its ID
     * 
     * @param id The answer option ID
     * @return The answer option
     */
    AnswerOptionDTO getAnswerOptionById(Long id);
    
    /**
     * Update an existing answer option
     * 
     * @param id The answer option ID to update
     * @param answerOptionDTO The updated answer option data
     * @return The updated answer option
     */
    AnswerOptionDTO updateAnswerOption(Long id, AnswerOptionDTO answerOptionDTO);
    
    /**
     * Delete an answer option
     * 
     * @param id The answer option ID to delete
     */
    void deleteAnswerOption(Long id);
}