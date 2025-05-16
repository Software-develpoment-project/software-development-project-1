package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerOptionRepo extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByQuestionId(Long questionId);
    
    Optional<AnswerOption> findByQuestionIdAndIsCorrect(Long questionId, boolean isCorrect);
}