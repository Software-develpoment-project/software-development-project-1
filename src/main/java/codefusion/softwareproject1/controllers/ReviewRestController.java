package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.dto.CreateReviewDTO;
import codefusion.softwareproject1.dto.ReviewDTO;
import codefusion.softwareproject1.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api") // Base path for review-related endpoints
@Tag(name = "Review Controller", description = "APIs for managing quiz reviews")
public class ReviewRestController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Create a new review for a quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Cannot review non-published quiz"),
            @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    @PostMapping("/quizzes/{quizId}/reviews") // quizId comes from path, review data from body
    public ResponseEntity<ReviewDTO> createReview(
            @Parameter(description = "ID of the quiz to review") @PathVariable Long quizId,
            @Valid @RequestBody CreateReviewDTO createReviewDTO) {
        // Ensure the DTO's quizId matches the path variable, or set it
        if (createReviewDTO.getQuizId() == null) {
            createReviewDTO.setQuizId(quizId);
        } else if (!createReviewDTO.getQuizId().equals(quizId)) {
            // Or throw an exception for mismatch
            return ResponseEntity.badRequest().build(); 
        }
        
        ReviewDTO createdReview = reviewService.createReview(createReviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all reviews and summary for a specific quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews and summary"),
            @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    @GetMapping("/quizzes/{quizId}/reviews")
    public ResponseEntity<Map<String, Object>> getReviewsForQuiz(
            @Parameter(description = "ID of the quiz") @PathVariable Long quizId) {
        Map<String, Object> reviewsAndSummary = reviewService.getReviewsAndSummaryByQuizId(quizId);
        return ResponseEntity.ok(reviewsAndSummary);
    }
    
    @Operation(summary = "Get a single review by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved review"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long reviewId) {
        ReviewDTO review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }


    @Operation(summary = "Update an existing review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not owner or quiz not published"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(
            @Parameter(description = "ID of the review to update") @PathVariable Long reviewId,
            @Valid @RequestBody CreateReviewDTO reviewUpdateDTO,
            @RequestHeader(value = "X-Student-Nickname", required = false) String studentNickname) { // Simple way to pass nickname for ownership check
        // For real auth, use Spring Security principal or JWT
        
        // Ensure the DTO has quizId set (could be from original review or implicitly known if not changing quiz)
        // If reviewUpdateDTO needs quizId for validation, ensure it's correctly passed or retrieved.
        // The current CreateReviewDTO has quizId as required for validation for `create`. For update, it's about the content.
        // The service will use the existing review's quiz.

        ReviewDTO updatedReview = reviewService.updateReview(reviewId, reviewUpdateDTO, studentNickname);
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(summary = "Delete a review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not owner"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "ID of the review to delete") @PathVariable Long reviewId,
            @RequestHeader(value = "X-Student-Nickname", required = false) String studentNickname) {
        reviewService.deleteReview(reviewId, studentNickname);
        return ResponseEntity.noContent().build();
    }
}