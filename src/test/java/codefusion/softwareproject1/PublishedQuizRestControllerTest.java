package codefusion.softwareproject1;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codefusion.softwareproject1.controllers.PublishedQuizRestController;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.service.QuizService;

class PublishedQuizRestControllerTest {

    private QuizService quizService;
    private PublishedQuizRestController publishedQuizRestController;

    @BeforeEach
    void setUp() {
        quizService = mock(QuizService.class);
        publishedQuizRestController = new PublishedQuizRestController(quizService);
    }

    @Test
    void getPublishedQuizzesReturnsEmptyListWhenNoQuizzesExist() {
        // Arrange
        when(quizService.getPublishedQuizzes()).thenReturn(Collections.emptyList());

        // Act
        ResponseEntity<List<QuizDTO>> response = publishedQuizRestController.getPublishedQuizzes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(quizService, times(1)).getPublishedQuizzes();
    }

    @Test
    void getPublishedQuizzesReturnsListOfPublishedQuizzes() {
        // Arrange
        QuizDTO quiz1 = new QuizDTO();
        quiz1.setId(1L);
        quiz1.setTitle("Sample Quiz");

        List<QuizDTO> quizList = List.of(quiz1);
        when(quizService.getPublishedQuizzes()).thenReturn(quizList);

        // Act
        ResponseEntity<List<QuizDTO>> response = publishedQuizRestController.getPublishedQuizzes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Sample Quiz", response.getBody().get(0).getTitle());

        verify(quizService, times(1)).getPublishedQuizzes();
    }
}