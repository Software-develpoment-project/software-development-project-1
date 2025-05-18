package codefusion.softwareproject1.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerSubmissionDTO {

    @NotNull(message = "Answer option ID cannot be null")
    private Long answerOptionId;
} 