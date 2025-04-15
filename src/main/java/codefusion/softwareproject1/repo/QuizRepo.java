package codefusion.softwareproject1.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import codefusion.softwareproject1.Models.QuizClass;
import java.util.List;

public interface QuizRepo extends JpaRepository<QuizClass, Long> {
    List<QuizClass> findByPublished(boolean published);
    List<QuizClass> findByTeacherId(Long teacherId);
    List<QuizClass> findByCategoriesId(int categoryId);

}
