<<<<<<< HEAD
 
=======
package codefusion.softwareproject1.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import codefusion.softwareproject1.Models.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByTitle(String title);
    List<Quiz> findByDescription(String description);
    Quiz findById(long id);
}
>>>>>>> 22c4c7a3c629be5557e84b87b9f8fad678d52f60
