package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import codefusion.softwareproject1.Models.ChoiceClass;
import java.util.List;

public interface ChoiceRepo extends JpaRepository<ChoiceClass, Long> {
    List<ChoiceClass> findByQuestionId(Long questionId);
    List<ChoiceClass> findByQuestionIdAndIsCorrect(Long questionId, boolean isCorrect);
} 