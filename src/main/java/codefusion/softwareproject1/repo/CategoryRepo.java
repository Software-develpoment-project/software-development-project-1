package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codefusion.softwareproject1.Models.CategoryClass;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryClass, Long> {
    List<CategoryClass> findByName(String name);
    List<CategoryClass> findByNameContainingIgnoreCase(String keyword);
} 