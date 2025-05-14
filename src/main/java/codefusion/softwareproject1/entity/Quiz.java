package codefusion.softwareproject1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private boolean published = false;

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column
    private Difficulty difficulty;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "quiz_categories",
        joinColumns = @JoinColumn(name = "quiz_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    
    // Add version field for optimistic locking
    @Version
    private Long version;
    
    // Helper methods to maintain bidirectional relationships
    public void addQuestion(Question question) {
        questions.add(question);
        question.setQuiz(this);
    }
    
    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setQuiz(null);
    }
    
    public void addCategory(Category category) {
        categories.add(category);
        category.getQuizzes().add(this);
    }
    
    public void removeCategory(Category category) {
        categories.remove(category);
        category.getQuizzes().remove(this);
    }
    public void addCategoryId(Long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        this.categories.add(category);
    }
}