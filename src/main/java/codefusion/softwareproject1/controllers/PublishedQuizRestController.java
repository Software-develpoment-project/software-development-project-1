package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.dto.CategoryDTO;
import codefusion.softwareproject1.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * REST Controller for the published quizzes API.
 * Only returns quizzes where published == true.
 * Following Single Responsibility Principle - only handles published quizzes.
 * Includes category information in the responses.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Published Quizzes", description = "API for accessing published quizzes")
public class PublishedQuizRestController {

    private final QuizService quizService;

    @Autowired
    public PublishedQuizRestController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * Get all published quizzes.
     * 
     * @return list of published quiz DTOs with their associated categories
     */
    @GetMapping("/published-quizzes")
    @Operation(
        summary = "Get all published quizzes", 
        description = "Retrieves all quizzes marked as published including their categories"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved list of published quizzes",
            content = @Content(
                mediaType = "application/json", 
                array = @ArraySchema(schema = @Schema(implementation = QuizDTO.class))
            )
        ),
        @ApiResponse(responseCode = "404", description = "No published quizzes found")
    })
    public ResponseEntity<List<QuizDTO>> getPublishedQuizzes() {
        List<QuizDTO> publishedQuizzes = quizService.getPublishedQuizzes();
        
        if (publishedQuizzes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(publishedQuizzes, HttpStatus.OK);
    }
    
    /**
     * Get published quizzes by category ID.
     * 
     * @param categoryId the ID of the category to filter by
     * @return list of published quiz DTOs in the specified category
     */
    
    
}