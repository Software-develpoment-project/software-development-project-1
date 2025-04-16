package codefusion.softwareproject1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceDTO {
   
    private String questionText;
    private boolean answer;
}
