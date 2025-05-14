package codefusion.softwareproject1.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import codefusion.softwareproject1.entity.Question;
import codefusion.softwareproject1.entity.Quiz.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for Quiz entity with validation.
 * Extended with version field for optimistic locking support.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizDTO {
    
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
    
    private boolean published;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Difficulty difficulty;
    
    private List<Long> categoryIds;
    
    
    private List<QuestionDTO> question ;
    
    // Add version field to support optimistic locking
    private Long version;
}