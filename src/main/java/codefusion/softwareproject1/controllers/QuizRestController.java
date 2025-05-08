package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.dto.AnswerOptionDTO;
import codefusion.softwareproject1.dto.QuestionDTO;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.service.AnswerOptionService;
import codefusion.softwareproject1.service.CategoryService;
import codefusion.softwareproject1.service.QuestionService;
import codefusion.softwareproject1.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * REST Controller for the Quiz API, refactored to follow SOLID principles.
 * Uses interfaces instead of concrete implementations for better testability 
 * and follows Dependency Inversion Principle.
 */
@RestController
@RequestMapping("/api/quizzes")
@Tag(name = "Quiz Management", description = "APIs for managing quizzes, questions, and answer options")
public class QuizRestController {

    private final QuizService quizService;
    private final QuestionService questionService;
    private final AnswerOptionService answerOptionService;
    private final CategoryService categoryService;

    @Autowired
    public QuizRestController(
            QuizService quizService,
            QuestionService questionService,
            AnswerOptionService answerOptionService,
            CategoryService categoryService) {
        this.categoryService = categoryService;
        this.quizService = quizService;
        this.questionService = questionService;
        this.answerOptionService = answerOptionService;
    }

    // =============== Quiz Endpoints ===============

    @PostMapping
    @Operation(summary = "Create a new quiz", description = "Creates a new quiz with the response QuizDto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Quiz created successfully",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = QuizDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<QuizDTO> createQuiz(
            @Parameter(description = "Quiz information", required = true) 
            @Valid @RequestBody QuizDTO quizDTO) {
        QuizDTO createdQuiz = quizService.createQuiz(quizDTO);
        return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all quizzes", description = "Retrieves a list of all quizzes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of quizzes",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = QuizDTO.class)))
    })
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        List<QuizDTO> quizzes = quizService.getAllQuizzes();
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get quiz by ID", description = "Retrieves a quiz by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved quiz",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = QuizDTO.class))),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<QuizDTO> getQuizById(
            @Parameter(description = "Quiz ID", required = true) 
            @PathVariable Long id) {
        QuizDTO quiz = quizService.getQuizById(id);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update quiz", description = "Updates an existing quiz with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz updated successfully",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = QuizDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<QuizDTO> updateQuiz(
            @Parameter(description = "Quiz ID", required = true) 
            @PathVariable Long id,
            @Parameter(description = "Updated quiz information", required = true) 
            @Valid @RequestBody QuizDTO quizDTO) {
        QuizDTO updatedQuiz = quizService.updateQuiz(id, quizDTO);
        return new ResponseEntity<>(updatedQuiz, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete quiz", description = "Deletes a quiz by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Quiz deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<Void> deleteQuiz(
            @Parameter(description = "Quiz ID", required = true) 
            @PathVariable Long id) {
        quizService.deleteQuiz(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // =============== Question Endpoints ===============

    @PostMapping("/{quizId}/questions")
    @Operation(summary = "Add question to quiz", description = "Adds a new question to an existing quiz")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Question added successfully",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = QuestionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<QuestionDTO> addQuestion(
            @Parameter(description = "Quiz ID", required = true) 
            @PathVariable Long quizId,
            @Parameter(description = "Question information", required = true) 
            @Valid @RequestBody QuestionDTO questionDTO) {
        // Ensure the path variable and DTO match
        questionDTO.setQuizId(quizId);
        QuestionDTO createdQuestion = questionService.addQuestion(questionDTO);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping("/{quizId}/questions")
    @Operation(summary = "Get questions by quiz ID", description = "Retrieves all questions for a specific quiz")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of questions",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = QuestionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    public ResponseEntity<List<QuestionDTO>> getQuestionsByQuizId(
            @Parameter(description = "Quiz ID", required = true) 
            @PathVariable Long quizId) {
        List<QuestionDTO> questions = questionService.getQuestionsByQuizId(quizId);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/questions/{id}")
    @Operation(summary = "Get question by ID", description = "Retrieves a question by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved question",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = QuestionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Question not found")
    })
    public ResponseEntity<QuestionDTO> getQuestionById(
            @Parameter(description = "Question ID", required = true) 
            @PathVariable Long id) {
        QuestionDTO question = questionService.getQuestionById(id);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @DeleteMapping("/questions/{id}")
    @Operation(summary = "Delete question", description = "Deletes a question by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Question deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Question not found")
    })
    public ResponseEntity<Void> deleteQuestion(
            @Parameter(description = "Question ID", required = true) 
            @PathVariable Long id) {
        questionService.deleteQuestion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // =============== Answer Option Endpoints ===============

    @PostMapping("/questions/{questionId}/answers")
    @Operation(summary = "Add answer option to question", description = "Adds a new answer option to an existing question")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Answer option added successfully",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = AnswerOptionDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Question not found")
    })
    public ResponseEntity<AnswerOptionDTO> addAnswerOption(
            @Parameter(description = "Question ID", required = true) 
            @PathVariable Long questionId,
            @Parameter(description = "Answer option information", required = true) 
            @Valid @RequestBody AnswerOptionDTO answerOptionDTO) {
        // Ensure the path variable and DTO match
        answerOptionDTO.setQuestionId(questionId);
        AnswerOptionDTO createdAnswerOption = answerOptionService.addAnswerOption(answerOptionDTO);
        return new ResponseEntity<>(createdAnswerOption, HttpStatus.CREATED);
    }

    @GetMapping("/questions/{questionId}/answers")
    @Operation(summary = "Get answer options by question ID", description = "Retrieves all answer options for a specific question")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of answer options",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = AnswerOptionDTO.class))),
        @ApiResponse(responseCode = "404", description = "Question not found")
    })
    public ResponseEntity<List<AnswerOptionDTO>> getAnswerOptionsByQuestionId(
            @Parameter(description = "Question ID", required = true) 
            @PathVariable Long questionId) {
        List<AnswerOptionDTO> answerOptions = answerOptionService.getAnswerOptionsByQuestionId(questionId);
        return new ResponseEntity<>(answerOptions, HttpStatus.OK);
    }

    @DeleteMapping("/answers/{id}")
    @Operation(summary = "Delete answer option", description = "Deletes an answer option by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Answer option deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Answer option not found")
    })
    public ResponseEntity<Void> deleteAnswerOption(
            @Parameter(description = "Answer option ID", required = true) 
            @PathVariable Long id) {
        answerOptionService.deleteAnswerOption(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}