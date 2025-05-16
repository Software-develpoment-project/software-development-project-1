package codefusion.softwareproject1.dto;

import codefusion.softwareproject1.entity.Question; // For Difficulty enum if needed directly
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResultDTO {
    private Long questionId;
    private String questionText;
    private String questionDifficulty; // Using String to represent enum, or use Question.DifficultyLevel directly
    private long totalAnswers;    // Using long for counts
    private long correctAnswers;
    private long wrongAnswers;
} 