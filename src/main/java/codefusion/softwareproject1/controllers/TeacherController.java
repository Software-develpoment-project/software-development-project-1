package codefusion.softwareproject1.controllers;

<<<<<<< HEAD
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.TeacherClass;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.TeacherRepo;


=======
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.dto.TeacherDTO;
import codefusion.softwareproject1.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Teacher API operations.
 * Follows Single Responsibility Principle by focusing only on teacher resource operations.
 */
@RestController
>>>>>>> 2cd1609e75a00b9bd52d6adb7ea9907bc0b2ae5f
@RequestMapping("/api/teachers")
@Tag(name = "Teacher Controller", description = "API for teacher management")
public class TeacherController {

    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    @Operation(summary = "Get all teachers", description = "Returns a list of all teachers")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved teachers")
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.getAllTeachers();
        return new ResponseEntity<>(teachers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID", description = "Returns a teacher by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved teacher"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Long id) {
        TeacherDTO teacher = teacherService.getTeacherById(id);
        return new ResponseEntity<>(teacher, HttpStatus.OK);
    }

    @GetMapping("/{id}/quizzes")
    @Operation(summary = "Get quizzes by teacher ID", description = "Returns all quizzes created by a specific teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved quizzes"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    public ResponseEntity<List<QuizDTO>> getQuizzesByTeacher(@PathVariable Long id) {
        List<QuizDTO> quizzes = teacherService.getQuizzesByTeacher(id);
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create teacher", description = "Creates a new teacher")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Teacher successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<TeacherDTO> createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        TeacherDTO createdTeacher = teacherService.createTeacher(teacherDTO);
        return new ResponseEntity<>(createdTeacher, HttpStatus.CREATED);
    }

<<<<<<< HEAD
        quiz.setName(updatedQuiz.getName());
        quiz.setDescription(updatedQuiz.getDescription());
        quiz.setCategories(updatedQuiz.getCategories());
        quiz.setPublished(updatedQuiz.isPublished());
        quiz.setQuestions(updatedQuiz.getQuestions());
    
        
=======
    @PutMapping("/{id}")
    @Operation(summary = "Update teacher", description = "Updates an existing teacher by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teacher successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    public ResponseEntity<TeacherDTO> updateTeacher(
            @PathVariable Long id,
            @Valid @RequestBody TeacherDTO teacherDTO) {
        TeacherDTO updatedTeacher = teacherService.updateTeacher(id, teacherDTO);
        return new ResponseEntity<>(updatedTeacher, HttpStatus.OK);
    }
>>>>>>> 2cd1609e75a00b9bd52d6adb7ea9907bc0b2ae5f

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete teacher", description = "Deletes an existing teacher by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Teacher successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
