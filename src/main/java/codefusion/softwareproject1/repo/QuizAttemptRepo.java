package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttemptRepo extends JpaRepository<QuizAttempt, Long> {
    // Custom query methods can be added here if needed, for example:
    // List<QuizAttempt> findByStudentId(Long studentId);
    // List<QuizAttempt> findByQuizId(Long quizId);
} 