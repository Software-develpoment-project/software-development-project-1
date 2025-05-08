package codefusion.softwareproject1.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codefusion.softwareproject1.entity.QuizReview;

@Repository
public interface QuizReviewRepo extends JpaRepository<QuizReview, Long> {
    // Custom query methods can be added here if needed
    // For example, find reviews by quiz ID or teacher ID
    List<QuizReview> findByQuizId(Long quizId);
    List<QuizReview> findByStudentId(Long studentId);
    List<QuizReview> findByid(Long id);

    
}
