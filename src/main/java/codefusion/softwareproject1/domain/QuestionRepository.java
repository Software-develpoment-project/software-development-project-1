<<<<<<< HEAD
 
=======
package codefusion.softwareproject1.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import codefusion.softwareproject1.Models.Questions;

@Repository
public interface QuestionRepository extends JpaRepository<Questions, Long> {
    List<Questions> findByQuizId(Long quizId);
    Questions findById(long id);
}
>>>>>>> 22c4c7a3c629be5557e84b87b9f8fad678d52f60
