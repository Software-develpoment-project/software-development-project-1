package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.entity.Question;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Question entity and QuestionDTO.
 * Follows Single Responsibility Principle by isolating mapping logic.
 */
@Component
public class QuestionMapper implements EntityMapper<Question, QuestionDTO> {
    
    private final QuizRepo quizRepository;
    
    @Autowired
    public QuestionMapper(QuizRepo quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public QuestionDTO toDto(Question entity) {
        if (entity == null) {
            return null;
        }
        
        QuestionDTO dto = new QuestionDTO();
        dto.setId(entity.getId());
        dto.setQuestionText(entity.getQuestionText());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        if (entity.getQuiz() != null) {
            dto.setQuizId(entity.getQuiz().getId());
        }
        
        return dto;
    }

    @Override
    public Question toEntity(QuestionDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Question entity = new Question();
        entity.setQuestionText(dto.getQuestionText());
        
        // Set quiz if quizId is provided
        if (dto.getQuizId() != null) {
            quizRepository.findById(dto.getQuizId())
                    .ifPresent(entity::setQuiz);
        }
        
        return entity;
    }

    @Override
    public Question updateEntityFromDto(QuestionDTO dto, Question entity) {
        if (dto == null || entity == null) {
            return entity;
        }
        
        entity.setQuestionText(dto.getQuestionText());
        
        // Update quiz if quizId has changed
        if (dto.getQuizId() != null && 
                (entity.getQuiz() == null || !entity.getQuiz().getId().equals(dto.getQuizId()))) {
            quizRepository.findById(dto.getQuizId())
                    .ifPresent(entity::setQuiz);
        }
        
        return entity;
    }
} 