package codefusion.softwareproject1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class QuestionDTO {
    private Long id;

    @NotBlank(message = "Question text is required")
    @Size(min = 5, max = 500, message = "Question text must be between 5 and 500 characters")
    private String questionText;

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    private String difficultyLevel;
    private Integer points;

    private List<AnswerOptionDTO> answerOptions;

    private Date createdAt;
    private Date updatedAt;

    // Constructor to initialize all fields 
    public QuestionDTO(Long id, String questionText, Long quizId, String difficultyLevel, Integer points, Date createdAt, Date updatedAt, List<AnswerOptionDTO> answerOptions) {
        this.id = id;
        this.questionText = questionText;
        this.quizId = quizId;
        this.difficultyLevel = difficultyLevel;
        this.points = points;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.answerOptions = answerOptions;
    }
}