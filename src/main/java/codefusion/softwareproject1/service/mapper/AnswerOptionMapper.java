package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.dto.AnswerOptionDTO;
import codefusion.softwareproject1.entity.AnswerOption;
import org.springframework.stereotype.Component;

@Component
public class AnswerOptionMapper {

    /**
     * Convert entity to DTO
     */
    public AnswerOptionDTO toDto(AnswerOption answerOption) {
        if (answerOption == null) {
            return null;
        }

        AnswerOptionDTO dto = new AnswerOptionDTO();
        dto.setId(answerOption.getId());
        dto.setText(answerOption.getText());
        dto.setIsCorrect(answerOption.getIsCorrect());
        dto.setExplanation(answerOption.getExplanation());
        dto.setCreatedAt(answerOption.getCreatedAt());
        dto.setUpdatedAt(answerOption.getUpdatedAt());
        
        if (answerOption.getQuestion() != null) {
            dto.setQuestionId(answerOption.getQuestion().getId());
        }
        
        return dto;
    }

    /**
     * Convert DTO to entity
     */
    public AnswerOption toEntity(AnswerOptionDTO dto) {
        if (dto == null) {
            return null;
        }

        AnswerOption answerOption = new AnswerOption();
        updateEntityFromDto(dto, answerOption);
        return answerOption;
    }

    /**
     * Update entity from DTO
     */
    public void updateEntityFromDto(AnswerOptionDTO dto, AnswerOption answerOption) {
        if (dto == null || answerOption == null) {
            return;
        }

        answerOption.setText(dto.getText());
        
        if (dto.getIsCorrect() != null) {
            answerOption.setIsCorrect(dto.getIsCorrect());
        }
        
        if (dto.getExplanation() != null) {
            answerOption.setExplanation(dto.getExplanation());
        }
        
        // Note: Question relation is handled by the service layer
    }
}