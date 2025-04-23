package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.Models.ChoiceClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoiceRepo extends JpaRepository<ChoiceClass, Long> {
    // Find choices by question id
    List<ChoiceClass> findByQuestionId(Long questionId);
    
    // Find correct choice for a question
    ChoiceClass findByQuestionIdAndCorrectTrue(Long questionId);
    
    // Delete all choices for a specific question
    void deleteByQuestionId(Long questionId);
}