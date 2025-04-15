package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.TeacherClass;

import java.util.List;

@Repository
public interface QuizRepo extends JpaRepository<QuizClass, Long> {
    List<QuizClass> findByName(String name);
    List<QuizClass> findByTeachers(TeacherClass teacher);
    List<QuizClass> findByPublished(boolean published);
}
