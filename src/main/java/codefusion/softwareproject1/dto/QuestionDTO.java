package codefusion.softwareproject1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Question entity with validation.
 * Added version field for optimistic locking support.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionDTO {
    
    private Long id;
    
    @NotBlank(message = "Question text is required")
    @Size(min = 3, max = 500, message = "Question text must be between 3 and 500 characters")
    private String questiontext;
    
    @NotNull(message = "Quiz ID is required")
    private Long quizId;
    
    // Add version field to support optimistic locking
    private Long version;
    
    // Related options
    private List<AnswerOptionDTO> options = new ArrayList<>();
}