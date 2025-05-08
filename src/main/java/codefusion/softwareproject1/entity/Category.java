package codefusion.softwareproject1.entity;

import java.util.List;

import jakarta.persistence.* ;
import lombok.* ;

@Entity
@Table(name = "category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToMany
    @JoinTable(
        name = "quiz_category",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "quiz_id")
    )
    private List<Quiz> quizzes;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}