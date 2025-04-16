package codefusion.softwareproject1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceDTO {
<<<<<<< HEAD
    private Long id;
    private String content;
    private boolean isCorrect;
    private Long questionId;
=======
   
    private String questionText;
    private boolean answer;
>>>>>>> main
}
