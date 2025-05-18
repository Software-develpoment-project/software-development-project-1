package codefusion.softwareproject1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnswerDTO {
    private Long id;
    private Long attemptId;
    private Long questionId;
    private String questionText; // Denormalized for convenience
    private Long chosenAnswerId;
    private String chosenAnswerText; // Denormalized
    private boolean correct;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor for easier creation
    public StudentAnswerDTO(Long id, Long attemptId, Long questionId, String questionText,
                            Long chosenAnswerId, String chosenAnswerText, boolean correct) {
        this.id = id;
        this.attemptId = attemptId;
        this.questionId = questionId;
        this.questionText = questionText;
        this.chosenAnswerId = chosenAnswerId;
        this.chosenAnswerText = chosenAnswerText;
        this.correct = correct;
        // Timestamps can be added separately if needed by mapper
    }
}