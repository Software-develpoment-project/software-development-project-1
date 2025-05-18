package codefusion.softwareproject1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor

public class QuizDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    private String description;

    @Size(max = 50, message = "Course code must be less than 50 characters")
    private String courseCode;

    private boolean published;
    private Long categoryId;
    private CategoryDTO category;

    private List<QuestionDTO> questions;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuizDTO(Long id, String title, String description, String courseCode, boolean published,
                   Long categoryId, CategoryDTO category, LocalDateTime createdAt, LocalDateTime updatedAt,
                   List<QuestionDTO> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.courseCode = courseCode;
        this.published = published;
        this.categoryId = categoryId;
        this.category = category;
        this.questions = questions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public QuizDTO(Long id, String title, String description, String courseCode, boolean published, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.courseCode = courseCode;
        this.published = published;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        
    }
}