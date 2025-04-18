package codefusion.softwareproject1.Models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "choice")
public class ChoiceClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "content", nullable = false)
    private String content;
    
    @Column(name = "is_correct")
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionsClass question;

    // Getter to provide backwards compatibility with existing code
    public String getOptionText() {
        return this.content;
    }
    
    // Setter to provide backwards compatibility with existing code
    public void setOptionText(String optionText) {
        this.content = optionText;
    }
}
