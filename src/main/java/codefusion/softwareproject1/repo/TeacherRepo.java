package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.Models.TeacherClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<TeacherClass, Long> {
    // Find teacher by email
    Optional<TeacherClass> findByEmail(String email);
    
    // Find teachers by name containing keyword
    List<TeacherClass> findByNameContainingIgnoreCase(String keyword);
    
    // Check if a teacher with a specific email exists
    boolean existsByEmail(String email);
}