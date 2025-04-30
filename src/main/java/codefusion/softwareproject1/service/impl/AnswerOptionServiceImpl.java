package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.entity.AnswerOption;
import codefusion.softwareproject1.entity.Question;
import codefusion.softwareproject1.dto.AnswerOptionDTO;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.AnswerOptionRepo;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.service.AnswerOptionService;
import codefusion.softwareproject1.service.mapper.AnswerOptionMapper;
import codefusion.softwareproject1.service.validation.AnswerOptionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of AnswerOptionService interface.
 * Follows:
 * - Single Responsibility Principle: focuses only on answer option operations
 * - Open/Closed Principle: can be extended without modification
 * - Dependency Inversion Principle: depends on abstractions (interfaces)
 */
@Service
public class AnswerOptionServiceImpl implements AnswerOptionService {

    private final AnswerOptionRepo answerOptionRepository;
    private final QuestionRepo questionRepository;
    private final AnswerOptionMapper answerOptionMapper;
    private final AnswerOptionValidator validator;

    @Autowired
    public AnswerOptionServiceImpl(
            AnswerOptionRepo answerOptionRepository,
            QuestionRepo questionRepository,
            AnswerOptionMapper answerOptionMapper,
            AnswerOptionValidator validator) {
        this.answerOptionRepository = answerOptionRepository;
        this.questionRepository = questionRepository;
        this.answerOptionMapper = answerOptionMapper;
        this.validator = validator;
    }

    @Override
    public AnswerOptionDTO addAnswerOption(AnswerOptionDTO answerOptionDTO) {
        // Verify question exists
        Question question = questionRepository.findById(answerOptionDTO.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", answerOptionDTO.getQuestionId()));
        
        // Validate against business rules
        validator.validateMaxOptionsNotExceeded(question.getId());
        
        // Convert DTO to entity
        AnswerOption answerOption = answerOptionMapper.toEntity(answerOptionDTO);
        
        // Ensure question is set
        answerOption.setQuestion(question);
        
        // Save and return mapped DTO
        answerOption = answerOptionRepository.save(answerOption);
        return answerOptionMapper.toDto(answerOption);
    }

    @Override
    public List<AnswerOptionDTO> getAnswerOptionsByQuestionId(Long questionId) {
        // Verify question exists
        if (!questionRepository.existsById(questionId)) {
            throw new ResourceNotFoundException("Question", "id", questionId);
        }
        
        // Find answer options and map to DTOs
        return answerOptionRepository.findByQuestionId(questionId).stream()
                .map(answerOptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAnswerOption(Long id) {
        // Find the answer option
        AnswerOption answerOption = answerOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Answer Option", "id", id));
        
        // Validate against business rules
        validator.validateNotDeletingLastCorrectAnswer(answerOption);
        
        // Delete answer option
        answerOptionRepository.deleteById(id);
    }
} 