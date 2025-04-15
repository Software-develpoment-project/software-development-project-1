<<<<<<< HEAD
 
=======
package codefusion.softwareproject1.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import codefusion.softwareproject1.Models.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByName(String name);
    Teacher findByEmail(String email);
    List<Teacher> findByQuizID(String quizID);
}
>>>>>>> 22c4c7a3c629be5557e84b87b9f8fad678d52f60
