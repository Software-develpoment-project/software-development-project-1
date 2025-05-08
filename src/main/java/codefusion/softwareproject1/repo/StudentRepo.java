package codefusion.softwareproject1.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codefusion.softwareproject1.entity.Student;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {
    // Custom query methods can be defined here if needed
    // For example, find students by name, email, etc.
    List<Student> findByNameContainingIgnoreCase(String name);
    List<Student> findByEmail(String email);
    Optional<Student> findById(Long id);

}
