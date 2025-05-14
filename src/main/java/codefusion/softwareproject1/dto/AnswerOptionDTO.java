package codefusion.softwareproject1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerOptionDTO {
    private Long id;
    private String text;
    private Boolean isCorrect;
    private Long questionId;
    private String explanation;
    private Date createdAt;
    private Date updatedAt;
}