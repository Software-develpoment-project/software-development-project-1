package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codefusion.softwareproject1.Models.TeacherClass;

import java.util.List;

@Repository
public interface TeacherRepo extends JpaRepository<TeacherClass, Long> {
    List<TeacherClass> findByName(String name);
<<<<<<< HEAD
    TeacherClass findByEmail(String email);
    
=======
    List<TeacherClass> findByEmail(String email);
>>>>>>> 89c85ad9460c651327a987d99a608ec1898507c4
} 