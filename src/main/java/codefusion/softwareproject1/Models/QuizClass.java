package codefusion.softwareproject1.Models;

import jakarta.persistence.*;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

=======
import lombok.*;

import java.util.List;

>>>>>>> 07c750034530b1e52c8f58838cbc532d6bef1c8a
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD
@Data
=======
>>>>>>> 07c750034530b1e52c8f58838cbc532d6bef1c8a
@Table(name = "quiz")
public class QuizClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
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

    @Column(name = "published")
    private boolean published;

<<<<<<< HEAD
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionsClass> questions;
    
=======
>>>>>>> 07c750034530b1e52c8f58838cbc532d6bef1c8a
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherClass teacher;
}
