package codefusion.softwareproject1.dto;

import java.util.List;

import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.entity.QuizReview;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Long id;
    private String name;
    private String email ;
   
    List<QuizReview> quizReviews;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private class QuestionsResult{
        private Long questionId;
        private String selectedOptions ;
    }

    private List<QuestionsResult> questionsResults;
   


}
