package codefusion.softwareproject1.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import codefusion.softwareproject1.Models.QuestionsClass;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionsClass, Long> {
    List<QuestionsClass> findByQuizId(Long quizId);
    QuestionsClass findById(long id);
}
