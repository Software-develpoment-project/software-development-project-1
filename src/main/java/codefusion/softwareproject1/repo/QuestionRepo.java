package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import codefusion.softwareproject1.Models.QuestionsClass;
import java.util.List;

public interface QuestionRepo extends JpaRepository<QuestionsClass, Long> {
    List<QuestionsClass> findByQuizId(Long quizId);
    List<QuestionsClass> findByDifficultyLevel(QuestionsClass.DifficultyLevel difficultyLevel);
    List<QuestionsClass> findByQuizIdAndDifficultyLevel(Long quizId, QuestionsClass.DifficultyLevel difficultyLevel);
} 