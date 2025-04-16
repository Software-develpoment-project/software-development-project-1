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
>>>>>>> 89c85ad9460c651327a987d99a608ec1898507c4
}
