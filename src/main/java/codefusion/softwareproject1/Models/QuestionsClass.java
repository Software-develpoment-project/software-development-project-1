
package codefusion.softwareproject1.Models;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuestionsClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionText;
    private String answer;
    public enum DifficultyLevel {easy, medium, hard};

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<ChoiceClass> options;

   @ManyToOne
   @JoinColumn(name = "quiz_id", nullable = false)
   private QuizClass quiz ;
    
}