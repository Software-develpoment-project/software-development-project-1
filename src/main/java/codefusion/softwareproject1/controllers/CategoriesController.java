package codefusion.softwareproject1.controllers;


import codefusion.softwareproject1.dto.CategoryDTO;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.entity.Category;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
     */
    @GetMapping
    @Operation(summary = "Get all categories", description = "Returns a list of all categories.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "No categories found")
    })
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    /**
     * Get a category by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Returns a single category by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category retrieved successfully",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Optional<Category> categoryOpt = categoryService.getCategoryById(id);
        return categoryOpt.map(category -> ResponseEntity.ok(categoryService.convertToDTO(category)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new category
     */
    @PostMapping
    @Operation(summary = "Create a new category", description = "Creates and returns a new category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category createdCategory = categoryService.addCategory(categoryDTO);
        return new ResponseEntity<>(categoryService.convertToDTO(createdCategory), HttpStatus.CREATED);
    }

    /**
     * Update a category
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Updates an existing category by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDTO);
        if (updatedCategory != null) {
            return ResponseEntity.ok(categoryService.convertToDTO(updatedCategory));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete a category
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Deletes a category by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get all quizzes in a category
     */
    @GetMapping("/{id}/quizzes")
    @Operation(summary = "Get quizzes by category ID", description = "Returns all quizzes under a specific category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quizzes retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
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
     */
    @PostMapping("/{categoryId}/quizzes/{quizId}")
    @Operation(summary = "Add quiz to category", description = "Adds an existing quiz to a specific category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz added to category successfully",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category or quiz not found")
    })
    public ResponseEntity<CategoryDTO> addQuizToCategory(@PathVariable Long categoryId, @PathVariable Long quizId) {
        Category updatedCategory = categoryService.addQuizToCategory(categoryId, quizId);
        if (updatedCategory != null) {
            return ResponseEntity.ok(categoryService.convertToDTO(updatedCategory));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Remove a quiz from a category
     */
    @DeleteMapping("/{categoryId}/quizzes/{quizId}")
    @Operation(summary = "Remove quiz from category", description = "Removes a quiz from a specific category.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz removed from category successfully",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category or quiz not found")
    })
    public ResponseEntity<CategoryDTO> removeQuizFromCategory(@PathVariable Long categoryId, @PathVariable Long quizId) {
        Category updatedCategory = categoryService.removeQuizFromCategory(categoryId, quizId);
        if (updatedCategory != null) {
            return ResponseEntity.ok(categoryService.convertToDTO(updatedCategory));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Get categories by teacher
     */
    @GetMapping("/teacher/{teacherId}")
    @Operation(summary = "Get categories by teacher", description = "Returns all categories created by a specific teacher.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully",
                     content = @Content(mediaType = "application/json",
                     schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Teacher not found or no categories assigned")
    })
    public ResponseEntity<List<CategoryDTO>> getCategoriesByTeacher(@PathVariable Long teacherId) {
        List<Category> categories = categoryService.getCategoriesByTeacher(teacherId);
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }
}
