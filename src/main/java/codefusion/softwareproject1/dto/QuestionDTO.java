package codefusion.softwareproject1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    
    @NotBlank(message = "Question text is required")
    @Size(min = 5, max = 500, message = "Question text must be between 5 and 500 characters")
    private String questionText;
    
    @NotNull(message = "Quiz ID is required")
    private Long quizId;
    
    private Date createdAt;
    
    private Date updatedAt;
} 