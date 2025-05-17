package codefusion.softwareproject1;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codefusion.softwareproject1.controllers.QuizRestController;
import codefusion.softwareproject1.dto.AnswerOptionDTO;
import codefusion.softwareproject1.dto.QuestionDTO;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.service.AnswerOptionService;
import codefusion.softwareproject1.service.QuestionService;
import codefusion.softwareproject1.service.QuizService;

class QuizRestControllerTest {

    @Mock
    private QuizService quizService;

    @Mock
    private QuestionService questionService;

    @Mock
    private AnswerOptionService answerOptionService;

    @InjectMocks
    private QuizRestController quizRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== Quiz Endpoints ==========

    @Test
    void testCreateQuiz() {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setTitle("Test Quiz");

        when(quizService.createQuiz(any(QuizDTO.class))).thenReturn(quizDTO);

        ResponseEntity<QuizDTO> response = quizRestController.createQuiz(quizDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test Quiz", response.getBody().getTitle());
    }

    @Test
    void testGetAllQuizzes() {
        QuizDTO quiz1 = new QuizDTO();
        QuizDTO quiz2 = new QuizDTO();

        when(quizService.getAllQuizzes()).thenReturn(Arrays.asList(quiz1, quiz2));

        ResponseEntity<List<QuizDTO>> response = quizRestController.getAllQuizzes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetQuizById() {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(1L);

        when(quizService.getQuizById(1L)).thenReturn(quizDTO);

        ResponseEntity<QuizDTO> response = quizRestController.getQuizById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testUpdateQuiz() {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setTitle("Updated");

        when(quizService.updateQuiz(eq(1L), any(QuizDTO.class))).thenReturn(quizDTO);

        ResponseEntity<QuizDTO> response = quizRestController.updateQuiz(1L, quizDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated", response.getBody().getTitle());
    }

    @Test
    void testDeleteQuiz() {
        doNothing().when(quizService).deleteQuiz(1L);

        ResponseEntity<Void> response = quizRestController.deleteQuiz(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // ========== Question Endpoints ==========

    @Test
    void testAddQuestion() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setQuizId(1L);

        when(questionService.addQuestion(any(QuestionDTO.class))).thenReturn(questionDTO);

        ResponseEntity<QuestionDTO> response = quizRestController.addQuestion(1L, questionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, response.getBody().getQuizId());
    }

    @Test
    void testGetQuestionsByQuizId() {
        QuestionDTO q1 = new QuestionDTO();
        QuestionDTO q2 = new QuestionDTO();

        when(questionService.getQuestionsByQuizId(1L)).thenReturn(Arrays.asList(q1, q2));

        ResponseEntity<List<QuestionDTO>> response = quizRestController.getQuestionsByQuizId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetQuestionById() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(2L);

        when(questionService.getQuestionById(2L)).thenReturn(questionDTO);

        ResponseEntity<QuestionDTO> response = quizRestController.getQuestionById(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2L, response.getBody().getId());
    }

    @Test
    void testDeleteQuestion() {
        doNothing().when(questionService).deleteQuestion(2L);

        ResponseEntity<Void> response = quizRestController.deleteQuestion(2L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // ========== Answer Option Endpoints ==========

    @Test
    void testAddAnswerOption() {
        AnswerOptionDTO answerOptionDTO = new AnswerOptionDTO();
        answerOptionDTO.setQuestionId(5L);

        when(answerOptionService.addAnswerOption(any(AnswerOptionDTO.class))).thenReturn(answerOptionDTO);

        ResponseEntity<AnswerOptionDTO> response = quizRestController.addAnswerOption(5L, answerOptionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(5L, response.getBody().getQuestionId());
    }

    @Test
    void testGetAnswerOptionsByQuestionId() {
        AnswerOptionDTO a1 = new AnswerOptionDTO();
        AnswerOptionDTO a2 = new AnswerOptionDTO();

        when(answerOptionService.getAnswerOptionsByQuestionId(5L)).thenReturn(Arrays.asList(a1, a2));

        ResponseEntity<List<AnswerOptionDTO>> response = quizRestController.getAnswerOptionsByQuestionId(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testDeleteAnswerOption() {
        doNothing().when(answerOptionService).deleteAnswerOption(10L);

        ResponseEntity<Void> response = quizRestController.deleteAnswerOption(10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}