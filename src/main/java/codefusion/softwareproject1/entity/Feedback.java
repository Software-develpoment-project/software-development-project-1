package codefusion.softwareproject1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "message")
    private String message;
    
    @Column(name = "is_correct")
    private boolean isCorrect;
    
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
} 