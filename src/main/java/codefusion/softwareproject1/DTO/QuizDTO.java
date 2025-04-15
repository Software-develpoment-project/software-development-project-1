package codefusion.softwareproject1.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



// Removed duplicate class definition
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class QuizDTO {
    private String title;
    private String description;
    private QuestionsDTO[] questions;
}