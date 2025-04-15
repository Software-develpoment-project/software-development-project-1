package codefusion.softwareproject1.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import codefusion.softwareproject1.Models.QuizClass;

@Repository
public interface QuizRepository extends JpaRepository<QuizClass, Long> {
    List<QuizClass> findByTitle(String title);
    List<QuizClass> findByDescription(String description);
    QuizClass findById(long id);
}
