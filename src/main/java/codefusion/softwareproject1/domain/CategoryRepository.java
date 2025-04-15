package codefusion.softwareproject1.domain;

<<<<<<< HEAD
public interface CategoryRepository {

=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import codefusion.softwareproject1.Models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);
    Category findById(long id);
>>>>>>> 22c4c7a3c629be5557e84b87b9f8fad678d52f60
}
