package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepo extends JpaRepository<StudentAnswer, Long> {
    List<StudentAnswer> findByAttemptId(Long attemptId);
    List<StudentAnswer> findByQuestionIdIn(List<Long> questionIds); // For fetching answers for a set of questions in a quiz
    List<StudentAnswer> findByAttemptQuizId(Long quizId); // For fetching all answers related to a quiz, across all attempts
} 