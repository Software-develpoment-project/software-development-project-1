package codefusion.softwareproject1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codefusion.softwareproject1.dto.StudentDto;
import codefusion.softwareproject1.entity.QuizReview;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    /**
     * Get all quizzes for a student
     * 
     * @param studentId The ID of the student
     * @return ResponseEntity containing student data with quiz reviews
     */
    @GetMapping("/{studentId}/quizzes")
    @Operation(summary = "Get all quizzes for a student", description = "Retrieves all quiz reviews for a specific student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved student quiz data"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentDto> getAllQuizzes(@PathVariable Long studentId) {
        StudentDto studentDto = studentService.getAllQuiz(studentId);
        
        if (studentDto == null) {
            throw new ResourceNotFoundException("Student", "id", studentId);
        }
        
        return new ResponseEntity<>(studentDto, HttpStatus.OK);
    }
    
    /**
     * Save a quiz review for a student
     * 
     * @param studentDto The student DTO containing quiz review data
     * @return ResponseEntity containing the saved quiz review
     */
    @PostMapping("/quizzes/review")
    @Operation(summary = "Save a quiz review", description = "Saves a new quiz review for a student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Successfully saved quiz review"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
        public ResponseEntity<QuizReview> saveQuizReview(@Valid @RequestBody StudentDto studentDto) {
            if (studentDto == null || studentDto.getId() == null) {
                throw new IllegalArgumentException("Student data is required");
            }
            
            if (studentDto.getQuizReviews() == null || studentDto.getQuizReviews().isEmpty()) {
                throw new IllegalArgumentException("Quiz review data is required");
            }
            
            QuizReview savedReview = studentService.saveQuizReview(studentDto);
            return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
        }
    }