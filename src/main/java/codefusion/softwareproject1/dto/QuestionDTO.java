package codefusion.softwareproject1.dto;

import codefusion.softwareproject1.entity.Question.DifficultyLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String text;
    private Integer points;
    private DifficultyLevel difficultyLevel;
    private Long quizId;
    private Date createdAt;
    private Date updatedAt;
    private List<AnswerOptionDTO> answerOptions;
}