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

import java.util.List;

/**
 * REST Controller for the Quiz API, refactored to follow SOLID principles.
 * Uses interfaces instead of concrete implementations for better testability 
 * and follows Dependency Inversion Principle.
 */
@RestController
@RequestMapping("/api/quizzes")
public class QuizRestController {

    private final QuizService quizService;
    private final QuestionService questionService;
    private final AnswerOptionService answerOptionService;
    private final CategoryService categoryService;

    @Autowired
    public QuizRestController(
            QuizService quizService,
            QuestionService questionService,
            AnswerOptionService answerOptionService,CategoryService categoryService) {
        this.categoryService = categoryService;
        this.quizService = quizService;
        this.questionService = questionService;
        this.answerOptionService = answerOptionService;
    }

    // =============== Quiz Endpoints ===============

    @PostMapping
    public ResponseEntity<QuizDTO> createQuiz(@Valid @RequestBody QuizDTO quizDTO) {
        QuizDTO createdQuiz = quizService.createQuiz(quizDTO);
        return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        List<QuizDTO> quizzes = quizService.getAllQuizzes();
        return new ResponseEntity<>(quizzes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> getQuizById(@PathVariable Long id) {
        QuizDTO quiz = quizService.getQuizById(id) ;
        return new ResponseEntity<>(quiz, HttpStatus.OK);
        
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizDTO> updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody QuizDTO quizDTO) {
        QuizDTO updatedQuiz = quizService.updateQuiz(id, quizDTO);
        return new ResponseEntity<>(updatedQuiz, HttpStatus.OK) ;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // =============== Question Endpoints ===============

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<QuestionDTO> addQuestion(
            @PathVariable Long quizId,
            @Valid @RequestBody QuestionDTO questionDTO) {
        // Ensure the path variable and DTO match
        questionDTO.setQuizId(quizId);
        QuestionDTO createdQuestion = questionService.addQuestion(questionDTO);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByQuizId(@PathVariable Long quizId) {
        List<QuestionDTO> questions = questionService.getQuestionsByQuizId(quizId);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        QuestionDTO question = questionService.getQuestionById(id);
        return new ResponseEntity<>(question, HttpStatus.OK);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // =============== Answer Option Endpoints ===============

    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<AnswerOptionDTO> addAnswerOption(
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerOptionDTO answerOptionDTO) {
        // Ensure the path variable and DTO match
        answerOptionDTO.setQuestionId(questionId);
        AnswerOptionDTO createdAnswerOption = answerOptionService.addAnswerOption(answerOptionDTO);
        return new ResponseEntity<>(createdAnswerOption, HttpStatus.CREATED);
    }

    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<List<AnswerOptionDTO>> getAnswerOptionsByQuestionId(@PathVariable Long questionId) {
        List<AnswerOptionDTO> answerOptions = answerOptionService.getAnswerOptionsByQuestionId(questionId);
        return new ResponseEntity<>(answerOptions, HttpStatus.OK);
    }

    @DeleteMapping("/answers/{id}")
    public ResponseEntity<Void> deleteAnswerOption(@PathVariable Long id) {
        answerOptionService.deleteAnswerOption(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
} 