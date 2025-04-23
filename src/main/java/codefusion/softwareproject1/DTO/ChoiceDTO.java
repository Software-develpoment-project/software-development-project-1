package codefusion.softwareproject1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceDTO {
<<<<<<< HEAD

   
    private String questionText;
    private boolean answer;

=======
    private Long id;
    private String content;
    private boolean isCorrect;
    private Long questionId;
>>>>>>> e8484230e47fde06b264f1e486e12e09a85809ec
}
