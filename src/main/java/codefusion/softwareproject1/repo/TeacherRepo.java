package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import codefusion.softwareproject1.Models.TeacherClass;
import java.util.Optional;

public interface TeacherRepo extends JpaRepository<TeacherClass, Long> {
    Optional<TeacherClass> findByEmail(String email);
    boolean existsByEmail(String email);
} 