package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.dto.AnswerOptionDTO;
import codefusion.softwareproject1.entity.AnswerOption;
import codefusion.softwareproject1.entity.Question;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.AnswerOptionRepo;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.service.AnswerOptionService;
import codefusion.softwareproject1.service.mapper.AnswerOptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of AnswerOptionService for answer option operations.
 * Added optimistic locking and retry capabilities.
 */
@Service
public class AnswerOptionServiceImpl implements AnswerOptionService {

    private static final Logger logger = LoggerFactory.getLogger(AnswerOptionServiceImpl.class);
    
    private final AnswerOptionRepo answerOptionRepository;
    private final QuestionRepo questionRepository;
    private final AnswerOptionMapper answerOptionMapper;

    @Autowired
    public AnswerOptionServiceImpl(
            AnswerOptionRepo answerOptionRepository,
            QuestionRepo questionRepository,
            AnswerOptionMapper answerOptionMapper) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
        this.answerOptionMapper = answerOptionMapper;
    }

    @Override
    @Transactional
    @Retryable(
        value = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public AnswerOptionDTO addAnswerOption(AnswerOptionDTO answerOptionDTO) {
        logger.info("Adding new answer option to question ID: {}", answerOptionDTO.getQuestionId());
        
        try {
            // Find the question
            Question question = questionRepository.findById(answerOptionDTO.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question", "id", answerOptionDTO.getQuestionId()));
            
            // Convert DTO to entity
            AnswerOption answerOption = answerOptionMapper.toEntity(answerOptionDTO);
            answerOption.setQuestion(question);
            
            // Save answer option
            answerOption = answerOptionRepository.save(answerOption);
            
            // Add to question's answer options
            question.addAnswerOption(answerOption);
            
            logger.info("Answer option added successfully with ID: {}", answerOption.getId());
            
            return answerOptionMapper.toDto(answerOption);
        } catch (OptimisticLockingFailureException e) {
            logger.warn("Optimistic locking failure while adding answer option to question ID: {}. Will retry operation.", 
                      answerOptionDTO.getQuestionId());
            throw e;  // Let the @Retryable handle this
        } catch (Exception e) {
            logger.error("Error adding answer option to question ID {}: {}", 
                      answerOptionDTO.getQuestionId(), e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerOptionDTO> getAnswerOptionsByQuestionId(Long questionId) {
        logger.info("Retrieving answer options for question ID: {}", questionId);
        
        // Check if question exists
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        
        return answerOptionRepository.findByQuestionId(questionId).stream()
                .map(answerOptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AnswerOptionDTO getAnswerOptionById(Long id) {
        logger.info("Retrieving answer option ID: {}", id);
        
        AnswerOption answerOption = answerOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AnswerOption", "id", id));
        
        return answerOptionMapper.toDto(answerOption);
    }

    @Override
    @Transactional
    @Retryable(
        value = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public AnswerOptionDTO updateAnswerOption(Long id, AnswerOptionDTO answerOptionDTO) {
        logger.info("Updating answer option ID: {}", id);
        
        AnswerOption existingAnswerOption = answerOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AnswerOption", "id", id));
        
        try {
            // Update answer option fields from DTO
            answerOptionMapper.updateEntityFromDto(answerOptionDTO, existingAnswerOption);
            
            // If question ID has changed, update the question relationship
            if (answerOptionDTO.getQuestionId() != null && 
                (existingAnswerOption.getQuestion() == null || 
                !answerOptionDTO.getQuestionId().equals(existingAnswerOption.getQuestion().getId()))) {
                
                Question newQuestion = questionRepository.findById(answerOptionDTO.getQuestionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Question", "id", 
                                                                       answerOptionDTO.getQuestionId()));
                
                // Remove from old question if exists
                if (existingAnswerOption.getQuestion() != null) {
                    existingAnswerOption.getQuestion().removeAnswerOption(existingAnswerOption);
                }
                
                // Set new question and add to its collection
                existingAnswerOption.setQuestion(newQuestion);
                newQuestion.addAnswerOption(existingAnswerOption);
            }
            
            // Save updated answer option
            existingAnswerOption = answerOptionRepository.save(existingAnswerOption);
            logger.info("Answer option updated successfully: {}", existingAnswerOption.getId());
            
            return answerOptionMapper.toDto(existingAnswerOption);
        } catch (OptimisticLockingFailureException e) {
            logger.warn("Optimistic locking failure while updating answer option ID: {}. Will retry operation.", id);
            throw e;  // Let the @Retryable handle this
        } catch (Exception e) {
            logger.error("Error updating answer option ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    @Retryable(
        value = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public void deleteAnswerOption(Long id) {
        logger.info("Deleting answer option ID: {}", id);
        
        AnswerOption answerOption = answerOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AnswerOption", "id", id));
        
        try {
            // Remove from question's collection
            Question question = answerOption.getQuestion();
            if (question != null) {
                question.removeAnswerOption(answerOption);
            }
            
            answerOptionRepository.deleteById(id);
            logger.info("Answer option deleted successfully: {}", id);
        } catch (OptimisticLockingFailureException e) {
            logger.warn("Optimistic locking failure while deleting answer option ID: {}. Will retry operation.", id);
            throw e;  // Let the @Retryable handle this
        } catch (Exception e) {
            logger.error("Error deleting answer option ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}