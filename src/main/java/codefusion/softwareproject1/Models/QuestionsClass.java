package codefusion.softwareproject1.Models;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import jakarta.persistence.*;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question")
public class QuestionsClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "content", nullable = false)
    private String content;
    
    public enum DifficultyLevel {
        EASY, MEDIUM, HARD
    };

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChoiceClass> choices;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizClass quiz;
<<<<<<< HEAD
=======
    
    
>>>>>>> 07c750034530b1e52c8f58838cbc532d6bef1c8a
    
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedbackClass> feedbacks;
}