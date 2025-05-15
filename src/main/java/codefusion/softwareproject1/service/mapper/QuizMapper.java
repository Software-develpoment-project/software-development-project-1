package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.repo.CategoryRepo;
import codefusion.softwareproject1.entity.Category;
import codefusion.softwareproject1.entity.Question;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.dto.CategoryDTO;
import codefusion.softwareproject1.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;

/**
 * Mapper for converting between Quiz entity and QuizDTO.
 * Follows Single Responsibility Principle by isolating mapping logic.
 * Enhanced with null checks and defensive copying to prevent errors.
 */
@Component
public class QuizMapper implements EntityMapper<Quiz, QuizDTO> {

    @Autowired
    private CategoryMapper categoryMapper;
 
    @Autowired
    private QuestionMapper questionMapper;

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
       
        
        // Handle categories safely
        if (entity.getCategories() != null && !entity.getCategories().isEmpty()) {
            // Set category IDs
            List<Long> categoryIds = entity.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList());
            dto.setCategoryIds(categoryIds);
        } else {
            dto.setCategoryIds(new ArrayList<>());
        }
    
        // Map questions safely
        if (entity.getQuestions() != null && !entity.getQuestions().isEmpty()) {
            dto.setQuestion(entity.getQuestions().stream()
                .map(question -> questionMapper.toDto(question))
                .filter(qDto -> qDto != null) // Filter out nulls
                .collect(Collectors.toList()));
        } else {
            dto.setQuestion(new ArrayList<>());
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
        
        // Initialize collections to prevent null pointer exceptions
       entity.setCategories(new ArrayList<>());
        entity.setQuestions(new ArrayList<>());
       
        
        // Map categories if provided
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<Category> categories = dto.getCategoryIds().stream()
                .map(categoryId -> {
                    Category category = new Category();
                    category.setId(categoryId);
                    return category;
                })
                .filter(cat -> cat != null) // Filter out nulls
                .collect(Collectors.toList());
            entity.setCategories(categories);
        } else {
            entity.setCategories(Collections.emptyList());
        }

        // Map questions if provided
        if (dto.getQuestion() != null && !dto.getQuestion().isEmpty()) {
            List<Question> questions = dto.getQuestion().stream()
                .map(questionMapper::toEntity)
                .filter(q -> q != null) // Filter out nulls
                .collect(Collectors.toList());
            entity.setQuestions(questions);
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
        }
        
        // Explicitly check for boolean field changes
        entity.setPublished(dto.isPublished());
        
        if (dto.getDifficulty() != null) {
            entity.setDifficulty(dto.getDifficulty());
        }
        
        // Handle category updates carefully
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            // We leave this empty here as it's safer to handle category relationships
            // in the service layer with proper transaction management
        }

        // Update questions if provided
        if (dto.getQuestion() != null) {
            // Clear existing questions to avoid duplication or stale data
            entity.getQuestions().clear();
            
            if (!dto.getQuestion().isEmpty()) {
                List<Question> questions = dto.getQuestion().stream()
                    .map(questionMapper::toEntity)
                    .filter(q -> q != null)
                    .collect(Collectors.toList());
                
                // Set the quiz reference on each question
                questions.forEach(question -> question.setQuiz(entity));
                
                entity.setQuestions(questions);
            }
        }
        if( dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()){
            List<Category> categories = dto.getCategoryIds().stream()
                .map(categoryId -> {
                    Category category = new Category();
                    category.setId(categoryId);
                    return category;
                })
                .filter(cat -> cat != null) // Filter out nulls
                .collect(Collectors.toList());
            entity.setCategories(categories);
        } else {
            entity.setCategories(Collections.emptyList());
        }
        
        return entity;
    }
}