package codefusion.softwareproject1.controllers;


import codefusion.softwareproject1.dto.CategoryDTO;
import codefusion.softwareproject1.entity.Category;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")

public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Get all categories
     * 
     * @return List of all categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    /**
     * Get a category by ID
     * 
     * @param id Category ID
     * @return Category if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        return categoryOpt.map(category -> ResponseEntity.ok(categoryService.convertToDTO(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new category
     * 
     * @param categoryDTO Category data
     * @return Created category
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category createdCategory = categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(categoryService.convertToDTO(createdCategory), HttpStatus.CREATED);
    }

    /**
     * Update a category
     * 
     * @param id Category ID
     * @param categoryDTO Updated category data
     * @return Updated category
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDTO);
        if (updatedCategory != null) {
            return ResponseEntity.ok(categoryService.convertToDTO(updatedCategory));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete a category
     * 
     * @param id Category ID
     * @return No content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get all quizzes in a category
     * 
     * @param id Category ID
     * @return List of quizzes
     */
    @GetMapping("/{id}/quizzes")
    public ResponseEntity<List<Quiz>> getQuizzesByCategory(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        if (categoryOpt.isPresent()) {
            List<Quiz> quizzes = categoryService.getQuizzesByCategory(id);
            return ResponseEntity.ok(quizzes);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Add a quiz to a category
     * 
     * @param categoryId Category ID
     * @param quizId Quiz ID
     * @return Updated category
     */
    @PostMapping("/{categoryId}/quizzes/{quizId}")
    public ResponseEntity<CategoryDTO> addQuizToCategory(@PathVariable Long categoryId, @PathVariable Long quizId) {
        Category updatedCategory = categoryService.addQuizToCategory(categoryId, quizId);
        if (updatedCategory != null) {
            return ResponseEntity.ok(categoryService.convertToDTO(updatedCategory));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Remove a quiz from a category
     * 
     * @param categoryId Category ID
     * @param quizId Quiz ID
     * @return Updated category
     */
    @DeleteMapping("/{categoryId}/quizzes/{quizId}")
    public ResponseEntity<CategoryDTO> removeQuizFromCategory(@PathVariable Long categoryId, @PathVariable Long quizId) {
        Category updatedCategory = categoryService.removeQuizFromCategory(categoryId, quizId);
        if (updatedCategory != null) {
            return ResponseEntity.ok(categoryService.convertToDTO(updatedCategory));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get categories by teacher
     * 
     * @param teacherId Teacher ID
     * @return List of categories
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByTeacher(@PathVariable Long teacherId) {
        List<Category> categories = categoryService.getCategoriesByTeacher(teacherId);
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }
}