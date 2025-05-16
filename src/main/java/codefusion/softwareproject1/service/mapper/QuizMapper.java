package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.entity.Category;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.dto.CategoryDTO;
import codefusion.softwareproject1.dto.QuestionDTO; 
import codefusion.softwareproject1.repo.CategoryRepo;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections; 
import java.util.List;      
import java.util.stream.Collectors; 

@Component
public class QuizMapper implements EntityMapper<Quiz, QuizDTO> {

    private final CategoryRepo categoryRepository;
    private final CategoryMapper categoryMapper;
    private final QuestionMapper questionMapper; 

    @Autowired
    public QuizMapper(CategoryRepo categoryRepository, CategoryMapper categoryMapper, QuestionMapper questionMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.questionMapper = questionMapper; 
    }

    @Override
    public QuizDTO toDto(Quiz entity) {
        if (entity == null) {
            return null;
        }

        QuizDTO dto = new QuizDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setCourseCode(entity.getCourseCode());
        dto.setPublished(entity.isPublished());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getCategory() != null) {
            dto.setCategoryId(entity.getCategory().getId());
            dto.setCategory(categoryMapper.toDto(entity.getCategory()));
        } else {
            dto.setCategory(null);
        }

        
        if (entity.getQuestions() != null && !entity.getQuestions().isEmpty()) {
            List<QuestionDTO> questionDTOs = entity.getQuestions().stream()
                    .map(questionMapper::toDto) 
                    .collect(Collectors.toList());
            dto.setQuestions(questionDTOs);
        } else {
            dto.setQuestions(Collections.emptyList());
        }

        return dto;
    }

    @Override
    public Quiz toEntity(QuizDTO dto) {
        if (dto == null) {
            return null;
        }

        Quiz entity = new Quiz();
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setCourseCode(dto.getCourseCode());
        entity.setPublished(dto.isPublished());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));
            entity.setCategory(category);
        }
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
        } else if (dto.getDescription() == null && entity.getDescription() != null) {
            entity.setDescription(null);
        }
        if (dto.getCourseCode() != null) {
            entity.setCourseCode(dto.getCourseCode());
        } else if (dto.getCourseCode() == null && entity.getCourseCode() != null) {
            entity.setCourseCode(null);
        }
        entity.setPublished(dto.isPublished());

        if (dto.getCategoryId() != null) {
            if (entity.getCategory() == null || !entity.getCategory().getId().equals(dto.getCategoryId())) {
                Category category = categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));
                entity.setCategory(category);
            }
        } else {
            entity.setCategory(null);
        }
        return entity;
    }
}