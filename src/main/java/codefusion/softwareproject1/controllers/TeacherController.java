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
public class TeacherController {

    @Autowired
    private TeacherRepo teacherRepository;

    @GetMapping("/teachers")
    public List<TeacherClass> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @PostMapping("/teachers")
    public TeacherClass createTeacher(@RequestBody TeacherClass teacher) {
        return teacherRepository.save(teacher);
    }
}