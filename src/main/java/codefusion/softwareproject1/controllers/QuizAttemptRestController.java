package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.dto.AnswerSubmissionDTO;
import codefusion.softwareproject1.dto.QuizAttemptDTO;
import codefusion.softwareproject1.dto.StudentAnswerDTO;
import codefusion.softwareproject1.dto.QuizResultDTO;
import codefusion.softwareproject1.entity.StudentAnswer;
import codefusion.softwareproject1.service.QuizAttemptService;
import codefusion.softwareproject1.service.mapper.StudentAnswerMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quiz-attempts")
@Tag(name = "Quiz Attempt Controller", description = "API for managing student quiz attempts and submissions")
public class QuizAttemptRestController {

    private final QuizAttemptService quizAttemptService;
    private final StudentAnswerMapper studentAnswerMapper;

    @Autowired
    public QuizAttemptRestController(QuizAttemptService quizAttemptService, StudentAnswerMapper studentAnswerMapper) {
        this.quizAttemptService = quizAttemptService;
        this.studentAnswerMapper = studentAnswerMapper;
    }

    @Operation(summary = "Start a new quiz attempt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quiz attempt started successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or quiz not publishable"),
            @ApiResponse(responseCode = "404", description = "Quiz or Student not found")
    })
    @PostMapping("/start")
    public ResponseEntity<QuizAttemptDTO> startQuizAttempt(@RequestBody Map<String, Long> payload) {
        Long quizId = payload.get("quizId");
        if (quizId == null) {
            return ResponseEntity.badRequest().build();
        }
        Long studentId = payload.getOrDefault("studentId", 1L); // Default studentId
        QuizAttemptDTO attemptDTO = quizAttemptService.startQuizAttempt(quizId, studentId);
        return new ResponseEntity<>(attemptDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Submit an answer for a question in an attempt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answer submitted successfully, returns StudentAnswerDTO"),
            @ApiResponse(responseCode = "400", description = "Invalid input or submission error"),
            @ApiResponse(responseCode = "404", description = "Attempt, Question, or AnswerOption not found")
    })
    @PostMapping("/{attemptId}/questions/{questionId}/submit-answer")
    public ResponseEntity<StudentAnswerDTO> submitAnswer(
            @PathVariable Long attemptId,
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerSubmissionDTO submissionDTO) {

        Long studentId = 1L; // Default studentId for the operation
        StudentAnswer savedStudentAnswerEntity = quizAttemptService.submitStudentAnswer(attemptId, questionId, submissionDTO, studentId);

        StudentAnswerDTO responseDto = studentAnswerMapper.toDto(savedStudentAnswerEntity);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Get aggregated results for a specific quiz")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved quiz results"),
            @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    @GetMapping("/quizzes/{quizId}/results")
    public ResponseEntity<QuizResultDTO> getQuizAggregatedResults(@PathVariable Long quizId) {
        QuizResultDTO results = quizAttemptService.getQuizResults(quizId);
        return ResponseEntity.ok(results);
    }
}
