<<<<<<< HEAD
 
=======
package codefusion.softwareproject1.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import codefusion.softwareproject1.Models.AnswerOptions;

@Repository
public interface ChoiceRepository extends JpaRepository<AnswerOptions, Long> {
    List<AnswerOptions> findByQuestionId(Long questionId);
    List<AnswerOptions> findByIsCorrect(boolean isCorrect);
}
>>>>>>> 22c4c7a3c629be5557e84b87b9f8fad678d52f60
