package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.Models.QuizClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepo extends JpaRepository<QuizClass, Long> {
    // Find quizzes by teacher id
    List<QuizClass> findByTeacherId(Long teacherId);
    
    // Find published quizzes
    List<QuizClass> findByPublishedTrue();
    
    // Find quizzes by topic
    List<QuizClass> findByTopic(QuizClass.Topic topic);
    
    // Find quizzes by difficulty
    List<QuizClass> findByDifficulty(QuizClass.Difficulty difficulty);
    
    // Find quizzes by name containing keyword (for search functionality)
    List<QuizClass> findByNameContainingIgnoreCase(String keyword);
}