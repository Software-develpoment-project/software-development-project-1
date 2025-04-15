package codefusion.softwareproject1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChoiceDTO {
   
    private String optionText;
    private boolean isCorrect;
}
