package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.entity.Question;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.dto.QuestionDTO;
import codefusion.softwareproject1.dto.AnswerOptionDTO; 
import codefusion.softwareproject1.entity.AnswerOption;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections; 
import java.util.List;      
import java.util.stream.Collectors; 

@Component
public class QuestionMapper implements EntityMapper<Question, QuestionDTO> {
    
    private final QuizRepo quizRepository;
    private final AnswerOptionMapper answerOptionMapper; 

    @Autowired
    public QuestionMapper(QuizRepo quizRepository, AnswerOptionMapper answerOptionMapper) {
        this.quizRepository = quizRepository;
        this.answerOptionMapper = answerOptionMapper;
    }

    @Override
    public QuestionDTO toDto(Question entity) {
        if (entity == null) {
            return null;
        }
        
        QuestionDTO dto = new QuestionDTO();
        dto.setId(entity.getId());
        dto.setQuestionText(entity.getQuestionText()); 
        dto.setPoints(entity.getPoints()); 
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        
        if (entity.getDifficultyLevel() != null) {
            dto.setDifficultyLevel(entity.getDifficultyLevel().name());
        }
        
        if (entity.getQuiz() != null) {
            dto.setQuizId(entity.getQuiz().getId());
        }

        
        if (entity.getAnswerOptions() != null && !entity.getAnswerOptions().isEmpty()) {
            List<AnswerOptionDTO> answerOptionDTOs = entity.getAnswerOptions().stream()
                    .map(answerOptionMapper::toDto) 
                    .collect(Collectors.toList());
            dto.setAnswerOptions(answerOptionDTOs);
        } else {
            dto.setAnswerOptions(Collections.emptyList());
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
        entity.setPoints(dto.getPoints() != null ? dto.getPoints() : 1); 
        
        if (dto.getDifficultyLevel() != null) {
            try {
                
                if ("NORMAL".equalsIgnoreCase(dto.getDifficultyLevel())) {
                    entity.setDifficultyLevel(Question.DifficultyLevel.MEDIUM);
                } else {
                    entity.setDifficultyLevel(Question.DifficultyLevel.valueOf(dto.getDifficultyLevel().toUpperCase()));
                }
            } catch (IllegalArgumentException e) {
                entity.setDifficultyLevel(Question.DifficultyLevel.MEDIUM); // Default
            }
        } else {
            entity.setDifficultyLevel(Question.DifficultyLevel.MEDIUM); // Default
        }
        
        if (dto.getQuizId() != null) {
            quizRepository.findById(dto.getQuizId())
                    .ifPresent(entity::setQuiz);
        }

       
        if (dto.getAnswerOptions() != null && !dto.getAnswerOptions().isEmpty()) {
            List<AnswerOption> answerOptions = dto.getAnswerOptions().stream()
                    .map(answerOptionDTO -> {
                        AnswerOption ao = answerOptionMapper.toEntity(answerOptionDTO);
                        ao.setQuestion(entity); 
                        return ao;
                    })
                    .collect(Collectors.toList());
            entity.setAnswerOptions(answerOptions);
        }
        
        return entity;
    }

    @Override
    public Question updateEntityFromDto(QuestionDTO dto, Question entity) {
        if (dto == null || entity == null) {
            return entity;
        }
        
        entity.setQuestionText(dto.getQuestionText());
        if (dto.getPoints() != null) {
            entity.setPoints(dto.getPoints());
        }
        
        if (dto.getDifficultyLevel() != null) {
             try {
                if ("NORMAL".equalsIgnoreCase(dto.getDifficultyLevel())) {
                    entity.setDifficultyLevel(Question.DifficultyLevel.MEDIUM);
                } else {
                    entity.setDifficultyLevel(Question.DifficultyLevel.valueOf(dto.getDifficultyLevel().toUpperCase()));
                }
            } catch (IllegalArgumentException e) {
              
            }
        }
        
        if (dto.getQuizId() != null && 
                (entity.getQuiz() == null || !entity.getQuiz().getId().equals(dto.getQuizId()))) {
            quizRepository.findById(dto.getQuizId())
                    .ifPresent(entity::setQuiz);
        }
        
        return entity;
    }
}