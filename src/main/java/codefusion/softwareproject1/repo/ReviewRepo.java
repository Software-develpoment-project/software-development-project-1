package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> { // Extends JpaRepository
    
    // Custom query method to find reviews by quiz ID, ordered by creation date descending
    List<Review> findByQuizIdOrderByCreatedAtDesc(Long quizId); 

    // JpaRepository automatically provides:
    // save(S entity), saveAll(Iterable<S> entities)
    // findById(ID id)
    // existsById(ID id)
    // findAll()
    // findAllById(Iterable<ID> ids)
    // count()
    // deleteById(ID id)
    // delete(T entity)
    // deleteAll(Iterable<? extends T> entities)
    // deleteAll()
    // ...and more
}