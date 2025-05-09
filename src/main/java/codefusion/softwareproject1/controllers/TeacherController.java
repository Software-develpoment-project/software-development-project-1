package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.dto.QuizDTO;
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
    public Teacher getTeacher(@PathVariable Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + id));
    }   
    @GetMapping("/{email}/quizzes")
    public List<Quiz> getQuizzes(@PathVariable String email) {
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + email));
        
        return quizRepository.findByTeacherId(teacher.getId());
    }

    @PostMapping("/add/quiz")
    public String createQuiz(@RequestBody Quiz quizData) {
        if (quizData.getTitle() == null || quizData.getDescription() == null) {
            return "Quiz title and description are required.";
        }

        quizRepository.save(quizData);
        return "Quiz created successfully";
    }

    
    @PostMapping("/add")
    public String addTeacher(@RequestBody Teacher teacher) {

        
        if (teacher.getName() == null || teacher.getEmail() == null) {
            return "Teacher name and email are required.";
            
        }

        teacherRepository.save(teacher);
        return "Teacher added successfully";
    }

    
}
