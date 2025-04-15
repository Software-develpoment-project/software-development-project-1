package codefusion.softwareproject1.domain;

<<<<<<< HEAD
public interface ChoiceRepository {

=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import codefusion.softwareproject1.Models.ChoiceClass;

@Repository
public interface ChoiceRepository extends JpaRepository<ChoiceClass, Long> {
    List<ChoiceClass> findByQuestionId(Long questionId);
    List<ChoiceClass> findByIsCorrect(boolean isCorrect);
>>>>>>> 9114bb19839d0ff9f53fbca5b39f84e4e33377e2
}
