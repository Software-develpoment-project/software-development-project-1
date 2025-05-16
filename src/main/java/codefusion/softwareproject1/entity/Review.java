package codefusion.softwareproject1.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Review {
        private Long id;
        private String text;
        private int rating;

        
        List<Quiz> quizzes;
}
