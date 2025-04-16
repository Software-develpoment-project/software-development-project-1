package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.TeacherClass;

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
    public List<QuizClass> getQuizzes(@PathVariable Long id) {
        TeacherClass teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + id));

        return quizRepository.findByTeacherId(id);
    }

    @PostMapping("/add/quiz")
    public String createQuiz(@RequestBody QuizClass quizDTO) {
        if (quizDTO.getName() == null || quizDTO.getDescription() == null) {
            return "Quiz name and description are required.";
        }

        quizRepository.save(quizDTO);
        return "Quiz created successfully";
    }

    @PostMapping("/edit/{id}")
    public String editQuiz(@PathVariable Long id, @RequestBody QuizClass updatedQuiz) throws IllegalAccessException {
        QuizClass quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalAccessException("Quiz not found with id: " + id));

        quiz.setName(updatedQuiz.getName());
        quiz.setDescription(updatedQuiz.getDescription());
        quiz.setPublished(updatedQuiz.published());

        quizRepository.save(quiz);
        return "Quiz updated successfully";
    }
}
