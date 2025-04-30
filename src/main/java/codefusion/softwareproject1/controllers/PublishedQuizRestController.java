package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for the published quizzes API.
 * Only returns quizzes where published == true.
 * Following Single Responsibility Principle - only handles published quizzes.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PublishedQuizRestController {

    private final QuizService quizService;

    @Autowired
    public PublishedQuizRestController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * Get all published quizzes.
     * 
     * @return list of published quiz DTOs
     */
    @GetMapping("/published-quizzes")
    public ResponseEntity<List<QuizDTO>> getPublishedQuizzes() {
        List<QuizDTO> publishedQuizzes = quizService.getPublishedQuizzes();
        return new ResponseEntity<>(publishedQuizzes, HttpStatus.OK);
    }
} 