package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.QuestionsClass;
import codefusion.softwareproject1.Models.TeacherClass;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuestionQuizController {

    @Autowired
    private QuestionRepo questionRepository;

    @Autowired
    private QuizRepo quizRepository;

    // CRUD for QuestionsClass
    @GetMapping("/{quizId}/questions")
    public List<QuestionsClass> getQuestionsByQuiz(@PathVariable Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    @PostMapping("/{quizId}/questions")
    public QuestionsClass addQuestionToQuiz(@PathVariable Long quizId, @RequestBody QuestionsClass question) {
        return quizRepository.findById(quizId)
                .map(quiz -> {
                    question.setQuiz(quiz);
                    return questionRepository.save(question);
                })
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }