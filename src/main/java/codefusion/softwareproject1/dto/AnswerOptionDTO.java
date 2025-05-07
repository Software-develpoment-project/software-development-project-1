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
public class AnswerOptionDTO {
    private Long id;
    
    @NotBlank(message = "Answer text is required")
    @Size(min = 1, max = 255, message = "Answer text must be between 1 and 255 characters")
    private String answerText;
    
    @NotNull(message = "Correct flag is required")
    private Boolean correct;
    
    @NotNull(message = "Question ID is required")
    private Long questionId;
    
    private Date createdAt;
    
    private Date updatedAt;
} 