package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.entity.AnswerOption;
import codefusion.softwareproject1.dto.AnswerOptionDTO;
import codefusion.softwareproject1.repo.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between AnswerOption entity and AnswerOptionDTO.
 * Follows Single Responsibility Principle by isolating mapping logic.
 */
@Component
public class AnswerOptionMapper implements EntityMapper<AnswerOption, AnswerOptionDTO> {
    
    private final QuestionRepo questionRepository;
    
    @Autowired
    public AnswerOptionMapper(QuestionRepo questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public AnswerOptionDTO toDto(AnswerOption entity) {
        if (entity == null) {
            return null;
        }
        
        AnswerOptionDTO dto = new AnswerOptionDTO();
        dto.setId(entity.getId());
        dto.setAnswerText(entity.getAnswerText());
        dto.setCorrect(entity.isCorrect());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        if (entity.getQuestion() != null) {
            dto.setQuestionId(entity.getQuestion().getId());
        }
        
        return dto;
    }

    @Override
    public AnswerOption toEntity(AnswerOptionDTO dto) {
        if (dto == null) {
            return null;
        }
        
        AnswerOption entity = new AnswerOption();
        entity.setAnswerText(dto.getAnswerText());
        entity.setCorrect(dto.getCorrect());
        
        // Set question if questionId is provided
        if (dto.getQuestionId() != null) {
            questionRepository.findById(dto.getQuestionId())
                    .ifPresent(entity::setQuestion);
        }
        
        return entity;
    }

    @Override
    public AnswerOption updateEntityFromDto(AnswerOptionDTO dto, AnswerOption entity) {
        if (dto == null || entity == null) {
            return entity;
        }
        
        entity.setAnswerText(dto.getAnswerText());
        entity.setCorrect(dto.getCorrect());
        
        // Update question if questionId has changed
        if (dto.getQuestionId() != null && 
                (entity.getQuestion() == null || !entity.getQuestion().getId().equals(dto.getQuestionId()))) {
            questionRepository.findById(dto.getQuestionId())
                    .ifPresent(entity::setQuestion);
        }
        
        return entity;
    }
} 