package codefusion.softwareproject1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptDTO {
    private Long id;
    private Long quizId;
    private String quizTitle;
    private LocalDateTime attemptDate;
    private Float score;
}
