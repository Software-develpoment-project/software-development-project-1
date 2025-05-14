package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.dto.QuestionDTO;
import codefusion.softwareproject1.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    private final AnswerOptionMapper answerOptionMapper;

    @Autowired
    public QuestionMapper(AnswerOptionMapper answerOptionMapper) {
        this.answerOptionMapper = answerOptionMapper;
    }

    /**
     * Convert entity to DTO
     */
    public QuestionDTO toDto(Question question) {
        if (question == null) {
            return null;
        }

        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setText(question.getQuestionText());
        dto.setPoints(question.getPoints());
        dto.setDifficultyLevel(question.getDifficultyLevel());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());
        
        if (question.getQuiz() != null) {
            dto.setQuizId(question.getQuiz().getId());
        }
        
        if (question.getAnswerOptions() != null) {
            dto.setAnswerOptions(
                question.getAnswerOptions().stream()
                    .map(answerOptionMapper::toDto)
                    .toList()
            );
        }
        
        return dto;
    }

    /**
     * Convert DTO to entity
     */
    public Question toEntity(QuestionDTO dto) {
        if (dto == null) {
            return null;
        }

        Question question = new Question();
        updateEntityFromDto(dto, question);
        return question;
    }

    /**
     * Update entity from DTO
     */
    public void updateEntityFromDto(QuestionDTO dto, Question question) {
        if (dto == null || question == null) {
            return;
        }

        question.setQuestionText(dto.getText());
        
        if (dto.getPoints() != null) {
            question.setPoints(dto.getPoints());
        }
        
        if (dto.getDifficultyLevel() != null) {
            question.setDifficultyLevel(dto.getDifficultyLevel());
        }
        
        // Note: Quiz relation is handled by the service layer
        // Note: Answer options are handled separately
    }
}