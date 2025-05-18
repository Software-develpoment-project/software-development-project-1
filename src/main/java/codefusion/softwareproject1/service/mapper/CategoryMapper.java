package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.dto.CategoryDTO;
import codefusion.softwareproject1.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(Category entity) {
        if (entity == null) {
            return null;
        }
        return new CategoryDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        Category entity = new Category();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public void updateEntityFromDto(CategoryDTO dto, Category entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
    }
} 
