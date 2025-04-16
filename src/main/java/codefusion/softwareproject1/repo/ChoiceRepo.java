package codefusion.softwareproject1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import codefusion.softwareproject1.Models.ChoiceClass;
import codefusion.softwareproject1.Models.QuestionsClass;

import java.util.List;

@Repository
public interface ChoiceRepo extends JpaRepository<ChoiceClass, Long> {
    List<ChoiceClass> findByQuestion(QuestionsClass question);
    List<ChoiceClass> findByIsCorrect(boolean isCorrect);
    List<ChoiceClass> findByQuestionAndIsCorrect(QuestionsClass question, boolean isCorrect);
} 