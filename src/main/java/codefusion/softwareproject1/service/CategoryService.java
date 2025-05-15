package codefusion.softwareproject1.service;

import codefusion.softwareproject1.dto.CategoryDTO;
import codefusion.softwareproject1.entity.Category;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.entity.Teacher;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.CategoryRepo;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for Category operations.
 * Enhanced with improved transaction handling and error prevention.
 */
@Service

public class CategoryService {
    
    @Autowired
    private CategoryRepo categoryRepo;
    
    @Autowired
    private QuizRepo quizRepo;
    
    @Autowired
    private TeacherRepo teacherRepo;
    
    /**
     * Get all categories
     * @return List of all categories
     */
    
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
    
    /**
     * Get a category by ID
     * @param id Category ID
     * @return Optional containing the category if found
     */
    
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepo.findById(id);
    }
    
    /**
     * Get all quizzes by category ID
     */
    
    public List<Quiz> getQuizzesByCategory(Long categoryId) {
        Optional<Category> categoryOpt = categoryRepo.findById(categoryId);
        if (categoryOpt.isPresent()) {
            // Create a copy to avoid lazy initialization issues
            return new ArrayList<>(categoryOpt.get().getQuizzes());
        }
        return new ArrayList<>(); // Return empty list if category not found
    }
    
    /**
     * Add a new category
     */
    
    public Category addCategory(CategoryDTO categoryDTO) {
        try {
            Category category = new Category();
            category.setTitle(categoryDTO.getTitle());
            category.setDescription(categoryDTO.getDescription());
            
            // Initialize collections to prevent null pointer exceptions
            category.setQuizzes(new ArrayList<>());
            
            // Set the teacher if provided
            if (categoryDTO.getTeacherId() != null) {
                Optional<Teacher> teacherOpt = teacherRepo.findById(categoryDTO.getTeacherId());
                teacherOpt.ifPresent(category::setTeacher);
            }
            
            // Save the category first to get an ID
            Category savedCategory = categoryRepo.saveAndFlush(category);
            
            // Add quizzes if provided (in a separate step to avoid circular references)
            if (categoryDTO.getQuizIds() != null && !categoryDTO.getQuizIds().isEmpty()) {
                List<Quiz> quizzes = quizRepo.findAllById(categoryDTO.getQuizIds());
                for (Quiz quiz : quizzes) {
                    if (!savedCategory.getQuizzes().contains(quiz)) {
                        savedCategory.getQuizzes().add(quiz);
                    }
                }
                savedCategory = categoryRepo.saveAndFlush(savedCategory);
            }
            
            return savedCategory;
        } catch (OptimisticLockingFailureException e) {
            throw new RuntimeException("The category was modified by another transaction. Please try again.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error creating category: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update an existing category
     */
    
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        try {
            Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
            
            if (categoryDTO.getTitle() != null) {
                category.setTitle(categoryDTO.getTitle());
            }
            
            if (categoryDTO.getDescription() != null) {
                category.setDescription(categoryDTO.getDescription());
            }
            
            // Update teacher if specified
            if (categoryDTO.getTeacherId() != null) {
                Optional<Teacher> teacherOpt = teacherRepo.findById(categoryDTO.getTeacherId());
                teacherOpt.ifPresent(category::setTeacher);
            }
            
            // Update quizzes if specified
            if (categoryDTO.getQuizIds() != null) {
                updateCategoryQuizzes(category, categoryDTO.getQuizIds());
            }
            
            return categoryRepo.saveAndFlush(category);
        }  catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating category: " + e.getMessage(), e);
        }
    }
    
    /**
     * Helper method to safely update category-quiz associations
     */
    protected void updateCategoryQuizzes(Category category, List<Long> quizIds) {
        // Create copy of current quizzes to avoid concurrent modification
        Set<Quiz> currentQuizzes = new HashSet<>(category.getQuizzes());
        
        // Remove quizzes not in the new list
        currentQuizzes.forEach(quiz -> {
            if (!quizIds.contains(quiz.getId())) {
                category.getQuizzes().remove(quiz);
            }
        });
        
        // Add new quizzes
        List<Quiz> newQuizzes = quizRepo.findAllById(quizIds);
        for (Quiz quiz : newQuizzes) {
            if (!category.getQuizzes().contains(quiz)) {
                category.getQuizzes().add(quiz);
            }
        }
    }
    
    /**
     * Delete a category by ID
     */
   
    public boolean deleteCategory(Long id) {
        try {
            if (categoryRepo.existsById(id)) {
                // Get the category with its quizzes
                Category category = categoryRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
                
                // Remove all quiz associations
                category.getQuizzes().clear();
                categoryRepo.saveAndFlush(category);
                
                // Now delete the category
                categoryRepo.deleteById(id);
                categoryRepo.flush();
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting category: " + e.getMessage(), e);
        }
    }
    
    /**
     * Add a quiz to a category
     */
    
    public Category addQuizToCategory(Long categoryId, Long quizId) {
        try {
            Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
                
            Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
            
            // Check if the quiz is already in the category
            boolean quizExists = category.getQuizzes().stream()
                .anyMatch(q -> q.getId().equals(quizId));
                
            if (!quizExists) {
                category.getQuizzes().add(quiz);
                return categoryRepo.saveAndFlush(category);
            }
            return category;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error adding quiz to category: " + e.getMessage(), e);
        }
    }
    
    /**
     * Remove a quiz from a category
     */
    
    public Category removeQuizFromCategory(Long categoryId, Long quizId) {
        try {
            Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
            
            category.getQuizzes().removeIf(quiz -> quiz.getId().equals(quizId));
            return categoryRepo.saveAndFlush(category);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error removing quiz from category: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get categories by teacher ID
     */
   
    public List<Category> getCategoriesByTeacher(Long teacherId) {
        return categoryRepo.findByTeacherId(teacherId);
    }
    
    /**
     * Get categories by quiz ID
     */
    
    public List<Category> getCategoriesByQuiz(Long quizId) {
        return categoryRepo.findByQuizzesId(quizId);
    }
    
    /**
     * Convert Category entity to CategoryDTO
     */
    public CategoryDTO convertToDTO(Category category) {
        if (category == null) {
            return null;
        }
        
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setTitle(category.getTitle());
        dto.setDescription(category.getDescription());
        
        if (category.getTeacher() != null) {
            dto.setTeacherId(category.getTeacher().getId());
        }
        
        if (category.getQuizzes() != null && !category.getQuizzes().isEmpty()) {
            List<Long> quizIds = category.getQuizzes().stream()
                .map(Quiz::getId)
                .collect(Collectors.toList());
            dto.setQuizIds(quizIds);
        } else {
            dto.setQuizIds(new ArrayList<>());
        }
        
        return dto;
    }
    
    public Category convertToEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Category category = new Category();
        category.setId(dto.getId());
        category.setTitle(dto.getTitle());
        category.setDescription(dto.getDescription());
        category.setQuizzes(new ArrayList<>()); // Initialize with empty list
        
        return category;
    }
}