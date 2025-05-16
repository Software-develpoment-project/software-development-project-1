package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Long> {
    // Find published quizzes
    List<Quiz> findByPublishedTrue();
    
    // Find quizzes by title containing keyword (for search functionality)
    List<Quiz> findByTitleContainingIgnoreCase(String keyword);
    
    // Find quizzes by teacher id
    List<Quiz> findByTeacherId(Long teacherId);

    List<Quiz> findByCategoriesIdAndPublishedTrue(Long categoryId);

}