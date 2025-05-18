package codefusion.softwareproject1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizResultDTO {
    // This DTO can be a wrapper if there's more meta-info about the quiz results overall.
    // Or, as per the prompt, it can simply be a list of QuestionResultDTOs directly if the endpoint returns List<QuestionResultDTO>.
    // For now, let's assume it might wrap the list and potentially add other quiz-level summary data later.
    private Long quizId;
    private String quizTitle;
    private List<QuestionResultDTO> questionResults;
    // Potentially add overall score, total questions, etc. here later.
} 