package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import codefusion.softwareproject1.Models.CategoryClass;
import java.util.List;

public interface CategoryRepo extends JpaRepository<CategoryClass, Integer> {
    List<CategoryClass> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
} 