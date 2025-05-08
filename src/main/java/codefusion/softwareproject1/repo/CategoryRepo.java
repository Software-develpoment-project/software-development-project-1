package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    // Find categories by teacher id
    List<Category> findByTeacherId(Long teacherId);
    
    // Find categories by title containing keyword (for search functionality)
    List<Category> findByTitleContainingIgnoreCase(String keyword);
    
    // Find categories that contain a specific quiz
    List<Category> findByQuizzesId(Long quizId);
    
    // Find categories by title and teacher id
    List<Category> findByTitleAndTeacherId(String title, Long teacherId);

    
}