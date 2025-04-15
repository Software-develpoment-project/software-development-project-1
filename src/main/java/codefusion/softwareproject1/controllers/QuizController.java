package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.QuestionsClass;
import codefusion.softwareproject1.Models.TeacherClass;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizRepo quizRepository;

    // CRUD for QuizClass
    @GetMapping
    @ResponseBody
    public List<QuizClass> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @PostMapping
    @ResponseBody
    public QuizClass createQuiz(@RequestBody QuizClass quiz) {
        return quizRepository.save(quiz);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<QuizClass> getQuizById(@PathVariable Long id) {
        return quizRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<QuizClass> updateQuiz(@PathVariable Long id, @RequestBody QuizClass updatedQuiz) {
        return quizRepository.findById(id)
                .map(quiz -> {
                    quiz.setName(updatedQuiz.getName());
                    quiz.setDescription(updatedQuiz.getDescription());
                    quiz.setCategories(updatedQuiz.getCategories());
                    quiz.setPublished(updatedQuiz.isPublished());
                    quiz.setQuestions(updatedQuiz.getQuestions());
                    quiz.setTeachers(updatedQuiz.getTeachers());
                    return ResponseEntity.ok(quizRepository.save(quiz));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Object> deleteQuiz(@PathVariable Long id) {
        return quizRepository.findById(id)
                .map(quiz -> {
                    quizRepository.delete(quiz);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}