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
<<<<<<< HEAD
=======
@Entity
>>>>>>> 07c750034530b1e52c8f58838cbc532d6bef1c8a
public class ChoiceClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionsClass question;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // getters and setters for id, correct, and question
}

