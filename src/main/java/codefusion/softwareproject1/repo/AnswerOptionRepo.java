package codefusion.softwareproject1.repo;

import codefusion.softwareproject1.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerOptionRepo extends JpaRepository<AnswerOption, Long> {
    // Find answer options by question id
    List<AnswerOption> findByQuestionId(Long questionId);
    
    // Find correct answer option for a question
    AnswerOption findByQuestionIdAndCorrectTrue(Long questionId);
    
    // Delete all answer options for a specific question
    void deleteByQuestionId(Long questionId);
}