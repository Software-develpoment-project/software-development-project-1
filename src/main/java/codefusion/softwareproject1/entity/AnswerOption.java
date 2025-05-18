package codefusion.softwareproject1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "answer_option")
@EntityListeners(AuditingEntityListener.class)
public class AnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "option_text", nullable = false)
    private String text;
    
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private Question question;
    
    @Column(name = "explanation")
    private String explanation;
    
    @Version
    private Integer version;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;
}