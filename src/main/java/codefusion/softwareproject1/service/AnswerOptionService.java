package codefusion.softwareproject1.service;

import codefusion.softwareproject1.dto.AnswerOptionDTO;
import java.util.List;

/**
 * Service interface for Answer Options operations following Interface Segregation Principle.
 * Contains only methods relevant to Answer Option entity operations.
 */
public interface AnswerOptionService {
    
    /**
     * Adds a new answer option to a question.
     *
     * @param answerOptionDTO the answer option data transfer object
     * @return the created answer option DTO with ID field populated
     * @throws ResourceNotFoundException if the question is not found
     * @throws ResponseStatusException if max number of options exceeded
     */
    AnswerOptionDTO addAnswerOption(AnswerOptionDTO answerOptionDTO);
    
    /**
     * Retrieves all answer options for a specific question.
     *
     * @param questionId the question ID
     * @return list of answer option DTOs
     * @throws ResourceNotFoundException if the question is not found
     */
    List<AnswerOptionDTO> getAnswerOptionsByQuestionId(Long questionId);
    
    /**
     * Deletes an answer option by its ID.
     *
     * @param id the answer option ID
     * @throws ResourceNotFoundException if the answer option is not found
     * @throws ResponseStatusException if trying to delete the only correct answer
     */
    void deleteAnswerOption(Long id);
} 