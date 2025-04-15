package codefusion.softwareproject1.domain;

<<<<<<< HEAD
public interface TeacherRepository {

=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import codefusion.softwareproject1.Models.TeacherClass;

@Repository
public interface TeacherRepository extends JpaRepository<TeacherClass, Long> {
    List<TeacherClass> findByName(String name);
    TeacherClass findByEmail(String email);
    List<TeacherClass> findByQuizID(String quizID);
>>>>>>> 9114bb19839d0ff9f53fbca5b39f84e4e33377e2
}
