package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.TeacherClass;

import java.util.List;

@Repository
public interface QuizRepo extends JpaRepository<QuizClass, Long> {
    List<QuizClass> findByName(String name);
    List<QuizClass> findByPublished(boolean published);
<<<<<<< HEAD
    List<QuizClass> findByTeachersId(Long teacherId);
    
=======
    List<QuizClass> findByTeachers(TeacherClass teacher);
    List<QuizClass> findByNameContainingIgnoreCase(String keyword);
>>>>>>> 89c85ad9460c651327a987d99a608ec1898507c4
}
