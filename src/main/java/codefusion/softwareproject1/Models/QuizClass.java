package codefusion.softwareproject1.Models;

import java.util.List;
import jakarta.persistence.*;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuizClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private List<CategoryClass> categories ;

    private boolean published;

    @OneToMany(mappedBy = "question_id" )
    private List<QuestionsClass> questions;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private List<TeacherClass> teachers;

}
