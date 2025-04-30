package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {
    // Find teacher by email
    Optional<Teacher> findByEmail(String email);
    
    // Find teachers by name containing keyword
    List<Teacher> findByNameContainingIgnoreCase(String keyword);
    
    // Check if a teacher with a specific email exists
    boolean existsByEmail(String email);
}