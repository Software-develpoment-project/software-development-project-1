package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Long> {
    // Find questions by quiz id
    List<Question> findByQuizId(Long quizId);
    
    // Count questions for a specific quiz
    Long countByQuizId(Long quizId);
    
    // Find questions containing specific text (for search)
    List<Question> findByQuestionTextContainingIgnoreCase(String keyword);
}