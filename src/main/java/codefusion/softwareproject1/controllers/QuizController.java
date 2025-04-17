package codefusion.softwareproject1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import codefusion.softwareproject1.Models.QuestionsClass;
import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.TeacherClass;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.TeacherRepo;


@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizRepo quizRepository;

    @Autowired
    private QuestionRepo questionRepository;

    @Autowired
    private TeacherRepo teacherRepository;

    // CRUD for QuizClass
    @GetMapping
    public List<QuizClass> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @PostMapping
    public QuizClass createQuiz(@RequestBody QuizClass quiz) {
        return quizRepository.save(quiz);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizClass> getQuizById(@PathVariable Long id) {
        return quizRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizClass> updateQuiz(@PathVariable Long id, @RequestBody QuizClass updatedQuiz) {
        return quizRepository.findById(id)
                .map(quiz -> {
                    quiz.setName(updatedQuiz.getName());
                    quiz.setDescription(updatedQuiz.getDescription());
                    quiz.setCategories(updatedQuiz.getCategories());
                    quiz.setPublished(updatedQuiz.isPublished());
                    quiz.setQuestions(updatedQuiz.getQuestions());
                    quiz.setTeacher(updatedQuiz.getTeacher());
                    return ResponseEntity.ok(quizRepository.save(quiz));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteQuiz(@PathVariable Long id) {
        return quizRepository.findById(id)
                .map(quiz -> {
                    quizRepository.delete(quiz);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

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

    // CRUD for TeacherClass
    @GetMapping("/teachers")
    public List<TeacherClass> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @PostMapping("/teachers")
    public TeacherClass createTeacher(@RequestBody TeacherClass teacher) {
        return teacherRepository.save(teacher);
    }
}