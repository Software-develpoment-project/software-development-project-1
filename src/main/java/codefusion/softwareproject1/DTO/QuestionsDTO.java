package codefusion.softwareproject1.DTO;

import java.util.List;

import codefusion.softwareproject1.Models.QuestionsClass.DifficultyLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionsDTO {
    private Long id;
    private String content;
    private DifficultyLevel difficultyLevel;
    private Long quizId;
    private List<ChoiceDTO> choices;
}
