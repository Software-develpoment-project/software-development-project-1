package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.repo.CategoryRepo;
import codefusion.softwareproject1.entity.Category;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.List;

/**
 * Mapper for converting between Quiz entity and QuizDTO.
 * Follows Single Responsibility Principle by isolating mapping logic.
 */
@Component
public class QuizMapper implements EntityMapper<Quiz, QuizDTO> {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepo categoryRepo; 

    @Override
    public QuizDTO toDto(Quiz entity) {
        if (entity == null) {
            return null;
        }
        
        QuizDTO dto = new QuizDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setPublished(entity.isPublished());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setDifficulty(entity.getDifficulty());


        
        
    

        
        // Map question IDs if present
        if (entity.getQuestions() != null && !entity.getQuestions().isEmpty()) {
            List<Long> questionIds = entity.getQuestions().stream()
                .map(question -> question.getId())
                .collect(Collectors.toList());
            dto.setQuestionIds(questionIds);
        }
        
        return dto;
    }

    @Override
    public Quiz toEntity(QuizDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Quiz entity = new Quiz();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPublished(dto.isPublished());
        entity.setDifficulty(dto.getDifficulty());

        
        
        return entity;
    }

    @Override
    public Quiz updateEntityFromDto(QuizDTO dto, Quiz entity) {
        if (dto == null || entity == null) {
            return entity;
        }
        
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        
        if (dto.isPublished() != entity.isPublished()) {
            entity.setPublished(dto.isPublished());
        }
        
        if (dto.getDifficulty() != null) {
            entity.setDifficulty(dto.getDifficulty());
        }
        
        return entity;
    }
}