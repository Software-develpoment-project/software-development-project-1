
package codefusion.softwareproject1.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerOptions {
    private Long id;
    private String optionText;
    private boolean isCorrect;
}
