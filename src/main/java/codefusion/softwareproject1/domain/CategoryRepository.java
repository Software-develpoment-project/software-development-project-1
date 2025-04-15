package codefusion.softwareproject1.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import codefusion.softwareproject1.Models.CategoryClass;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryClass, Long> {
    List<CategoryClass> findByName(String name);
    CategoryClass findById(long id);
}
