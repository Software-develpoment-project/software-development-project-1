package codefusion.softwareproject1.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quiz")
public class QuizClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public enum Topic {
        MATH, SCIENCE, HISTORY, TECHNOLOGY
    }

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    private Topic topic;

    private boolean published;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherClass teacher;
}
