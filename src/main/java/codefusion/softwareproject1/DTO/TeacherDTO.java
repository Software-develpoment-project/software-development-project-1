package codefusion.softwareproject1.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {
    private String name; 
    private String email;
    private QuizDTO[] quizzes;
}
