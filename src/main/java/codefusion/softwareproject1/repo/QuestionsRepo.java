package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codefusion.softwareproject1.Models.QuestionsClass;
import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.QuestionsClass.DifficultyLevel;

import java.util.List;

@Repository
public interface QuestionsRepo extends JpaRepository<QuestionsClass, Long> {
    List<QuestionsClass> findByQuiz(QuizClass quiz);
    List<QuestionsClass> findByQuizId(Long quizId);
    List<QuestionsClass> findByDifficultyLevel(DifficultyLevel difficultyLevel);
    List<QuestionsClass> findByContentContaining(String keyword);
} 