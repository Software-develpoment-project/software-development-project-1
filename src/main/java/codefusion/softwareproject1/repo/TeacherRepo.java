package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codefusion.softwareproject1.Models.TeacherClass;

import java.util.List;

@Repository
public interface TeacherRepo extends JpaRepository<TeacherClass, Long> {
    List<TeacherClass> findByName(String name);
    List<TeacherClass> findByEmail(String email);
} 