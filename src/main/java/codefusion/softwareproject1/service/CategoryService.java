package codefusion.softwareproject1.service;

import codefusion.softwareproject1.dto.CategoryDTO;
import codefusion.softwareproject1.entity.Category;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.entity.Teacher;
import codefusion.softwareproject1.repo.CategoryRepo;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            return categoryOpt.get().getQuizzes();
        }
        return new ArrayList<>(); // Return empty list if category not found
    }
    
    /**
     * Add a new category
     
     */
    @Transactional
    public Category addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setTitle(categoryDTO.getTitle());
        category.setDescription(categoryDTO.getDescription());
        
        // Set the teacher if provided
        if (categoryDTO.getTeacherId() != null) {
            Optional<Teacher> teacherOpt = teacherRepo.findById(categoryDTO.getTeacherId());
            teacherOpt.ifPresent(category::setTeacher);
        }
        
        // Add quizzes if provided
        if (categoryDTO.getQuizIds() != null && !categoryDTO.getQuizIds().isEmpty()) {
            List<Quiz> quizzes = quizRepo.findAllById(categoryDTO.getQuizIds());
            category.setQuizzes(quizzes);
        }
        
        return categoryRepo.save(category);
    }
    
    /**
     * Update an existing category
     */
    @Transactional
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> categoryOpt = categoryRepo.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setTitle(categoryDTO.getTitle());
            category.setDescription(categoryDTO.getDescription());
            
            // Update teacher if specified
            if (categoryDTO.getTeacherId() != null) {
                Optional<Teacher> teacherOpt = teacherRepo.findById(categoryDTO.getTeacherId());
                teacherOpt.ifPresent(category::setTeacher);
            }
            
            // Update quizzes if specified
            if (categoryDTO.getQuizIds() != null) {
                List<Quiz> quizzes = quizRepo.findAllById(categoryDTO.getQuizIds());
                category.setQuizzes(quizzes);
            }
            
            return categoryRepo.save(category);
        }
        return null;
    }
    
    /**
     * Delete a category by ID
     */
    @Transactional
    public boolean deleteCategory(Long id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Add a quiz to a category
     */
    @Transactional
    public Category addQuizToCategory(Long categoryId, Long quizId) {
        Optional<Category> categoryOpt = categoryRepo.findById(categoryId);
        Optional<Quiz> quizOpt = quizRepo.findById(quizId);
        
        if (categoryOpt.isPresent() && quizOpt.isPresent()) {
            Category category = categoryOpt.get();
            Quiz quiz = quizOpt.get();
            
            if (!category.getQuizzes().contains(quiz)) {
                category.getQuizzes().add(quiz);
                return categoryRepo.save(category);
            }
            return category;
        }
        return null;
    }
    
    /**
     * Remove a quiz from a category
     */
    @Transactional
    public Category removeQuizFromCategory(Long categoryId, Long quizId) {
        Optional<Category> categoryOpt = categoryRepo.findById(categoryId);
        
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.getQuizzes().removeIf(quiz -> quiz.getId().equals(quizId));
            return categoryRepo.save(category);
        }
        return null;
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
        }
        
        return dto;
    }
    
    
    public Category convertToEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setTitle(dto.getTitle());
        category.setDescription(dto.getDescription());
        
        // Teacher and quizzes will be set separately based on IDs
        
        return category;
    }
}