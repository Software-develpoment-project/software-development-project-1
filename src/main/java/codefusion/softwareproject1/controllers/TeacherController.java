package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.entity.Teacher;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private QuizRepo quizRepository;

    @Autowired
    private TeacherRepo teacherRepository;

    @GetMapping("/{id}")
    public List<Quiz> getQuizzes(@PathVariable Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + id));

        return quizRepository.findByTeacherId(id);
    }

    @PostMapping("/add/quiz")
    public String createQuiz(@RequestBody Quiz quizData) {
        if (quizData.getTitle() == null || quizData.getDescription() == null) {
            return "Quiz title and description are required.";
        }

        quizRepository.save(quizData);
        return "Quiz created successfully";
    }

    @PostMapping("/edit/{id}")
    public String editQuiz(@PathVariable Long id, @RequestBody Quiz updatedQuiz) throws IllegalAccessException {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalAccessException("Quiz not found with id: " + id));

        quiz.setTitle(updatedQuiz.getTitle());
        quiz.setDescription(updatedQuiz.getDescription());
        quiz.setPublished(updatedQuiz.isPublished());

        quizRepository.save(quiz);
        return "Quiz updated successfully";
    }
}
