package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.dto.QuizDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Quiz entity and QuizDTO.
 * Follows Single Responsibility Principle by isolating mapping logic.
 */
@Component
public class QuizMapper implements EntityMapper<Quiz, QuizDTO> {

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
        
        return dto;
    }

    @Override
    public Quiz toEntity(QuizDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Quiz entity = new Quiz();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPublished(dto.isPublished());
        
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
        
        return entity;
    }
} 