package codefusion.softwareproject1.dto;

import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import codefusion.softwareproject1.entity.Student;
import codefusion.softwareproject1.entity.Quiz.Difficulty;

@Data
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
    
    private List<Long> categoryIds = new ArrayList<>();

    private List<Long> questionIds = new ArrayList<>();
    
} 