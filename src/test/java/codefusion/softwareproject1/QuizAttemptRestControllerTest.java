package codefusion.softwareproject1;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codefusion.softwareproject1.controllers.QuizAttemptRestController;
import codefusion.softwareproject1.dto.AnswerSubmissionDTO;
import codefusion.softwareproject1.dto.QuizAttemptDTO;
import codefusion.softwareproject1.dto.QuizResultDTO;
import codefusion.softwareproject1.dto.StudentAnswerDTO;
import codefusion.softwareproject1.entity.StudentAnswer;
import codefusion.softwareproject1.service.QuizAttemptService;
import codefusion.softwareproject1.service.mapper.StudentAnswerMapper;

class QuizAttemptRestControllerTest {

    private QuizAttemptService quizAttemptService;
    private StudentAnswerMapper studentAnswerMapper;
    private QuizAttemptRestController quizAttemptRestController;

    @BeforeEach
    void setUp() {
        quizAttemptService = mock(QuizAttemptService.class);
        studentAnswerMapper = mock(StudentAnswerMapper.class);
        quizAttemptRestController = new QuizAttemptRestController(quizAttemptService, studentAnswerMapper);
    }

    @Test
    void startQuizAttemptReturnsCreatedWhenQuizIdPresent() {
        // Arrange
        Map<String, Long> payload = Map.of("quizId", 1L, "studentId", 2L);
        QuizAttemptDTO attemptDTO = new QuizAttemptDTO();
        when(quizAttemptService.startQuizAttempt(1L, 2L)).thenReturn(attemptDTO);

        // Act
        ResponseEntity<QuizAttemptDTO> response = quizAttemptRestController.startQuizAttempt(payload);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(quizAttemptService, times(1)).startQuizAttempt(1L, 2L);
    }

    @Test
    void startQuizAttemptReturnsBadRequestWhenQuizIdMissing() {
        // Arrange
        Map<String, Long> payload = Collections.emptyMap();

        // Act
        ResponseEntity<QuizAttemptDTO> response = quizAttemptRestController.startQuizAttempt(payload);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(quizAttemptService, never()).startQuizAttempt(anyLong(), anyLong());
    }

    @Test
    void submitAnswerReturnsOkWithStudentAnswerDTO() {
        // Arrange
        Long attemptId = 1L;
        Long questionId = 10L;
        AnswerSubmissionDTO submissionDTO = new AnswerSubmissionDTO();
        StudentAnswer studentAnswer = new StudentAnswer();
        StudentAnswerDTO responseDto = new StudentAnswerDTO();

        when(quizAttemptService.submitStudentAnswer(eq(attemptId), eq(questionId), eq(submissionDTO), anyLong())).thenReturn(studentAnswer);
        when(studentAnswerMapper.toDto(studentAnswer)).thenReturn(responseDto);

        // Act
        ResponseEntity<StudentAnswerDTO> response = quizAttemptRestController.submitAnswer(attemptId, questionId, submissionDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(quizAttemptService, times(1)).submitStudentAnswer(eq(attemptId), eq(questionId), eq(submissionDTO), anyLong());
        verify(studentAnswerMapper, times(1)).toDto(studentAnswer);
    }

    @Test
    void getQuizAggregatedResultsReturnsOk() {
        // Arrange
        Long quizId = 5L;
        QuizResultDTO resultDTO = new QuizResultDTO();
        when(quizAttemptService.getQuizResults(quizId)).thenReturn(resultDTO);

        // Act
        ResponseEntity<QuizResultDTO> response = quizAttemptRestController.getQuizAggregatedResults(quizId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(quizAttemptService, times(1)).getQuizResults(quizId);
    }
}