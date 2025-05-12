package codefusion.softwareproject1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import codefusion.softwareproject1.entity.Quiz.Difficulty;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;
    
    private String description;
    
    private boolean published;

    private Difficulty difficulty;
    
    private Date createdAt;
    
    private Date updatedAt;
    
    // List of category DTOs to represent the associated categories
    private List<CategoryDTO> categories = new ArrayList<>();

    private List<Long> questionIds = new ArrayList<>();
    
    // For internal use in the service layer
    private List<Long> categoryIds = new ArrayList<>();
}