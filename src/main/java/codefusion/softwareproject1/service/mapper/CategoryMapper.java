package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.entity.Category;
import codefusion.softwareproject1.dto.CategoryDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Category entity and CategoryDTO.
 * Follows Single Responsibility Principle by isolating mapping logic.
 */
@Component
public class CategoryMapper implements EntityMapper<Category, CategoryDTO> {

    @Override
    public CategoryDTO toDto(Category entity) {
        if (entity == null) {
            return null;
        }
        
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        
        return dto;
    }

    @Override
    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Category entity = new Category();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        
        return entity;
    }

    @Override
    public Category updateEntityFromDto(CategoryDTO dto, Category entity) {
        if (dto == null || entity == null) {
            return entity;
        }
        
        if (dto.getTitle() != null) {
            entity.setTitle(dto.getTitle());
        }
        
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        
        return entity;
    }
}