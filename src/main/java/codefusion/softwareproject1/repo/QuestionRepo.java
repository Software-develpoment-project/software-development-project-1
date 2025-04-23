package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.Models.QuestionsClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<QuestionsClass, Long> {
    // Find questions by quiz id
    List<QuestionsClass> findByQuizId(Long quizId);
    
    // Find questions by difficulty level
    List<QuestionsClass> findByDifficultyLevel(QuestionsClass.DifficultyLevel difficultyLevel);
    
    // Find questions by quiz id and difficulty level
    List<QuestionsClass> findByQuizIdAndDifficultyLevel(Long quizId, QuestionsClass.DifficultyLevel difficultyLevel);
    
    // Count questions for a specific quiz
    Long countByQuizId(Long quizId);
    
    // Find questions containing specific content (for search)
    List<QuestionsClass> findByContentContainingIgnoreCase(String keyword);
}