package codefusion.softwareproject1;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import codefusion.softwareproject1.controllers.ReviewRestController;
import codefusion.softwareproject1.dto.CreateReviewDTO;
import codefusion.softwareproject1.dto.ReviewDTO;
import codefusion.softwareproject1.service.ReviewService;

class ReviewRestControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewRestController reviewRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReview_success() {
        Long quizId = 1L;
        CreateReviewDTO createReviewDTO = new CreateReviewDTO();
        // Don't set quizId here to test auto-set in controller
        ReviewDTO returnedReview = new ReviewDTO();
        returnedReview.setId(10L);

        when(reviewService.createReview(any(CreateReviewDTO.class))).thenReturn(returnedReview);

        ResponseEntity<ReviewDTO> response = reviewRestController.createReview(quizId, createReviewDTO);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(returnedReview, response.getBody());
        assertEquals(quizId, createReviewDTO.getQuizId());  // controller sets quizId if null

        verify(reviewService, times(1)).createReview(createReviewDTO);
    }

    @Test
    void createReview_quizIdMismatch_badRequest() {
        Long pathQuizId = 1L;
        CreateReviewDTO createReviewDTO = new CreateReviewDTO();
        createReviewDTO.setQuizId(2L);  // different than path

        ResponseEntity<ReviewDTO> response = reviewRestController.createReview(pathQuizId, createReviewDTO);

        assertEquals(400, response.getStatusCodeValue());
        verifyNoInteractions(reviewService);
    }

    @Test
    void getReviewsForQuiz_success() {
        Long quizId = 1L;
        Map<String, Object> dummyMap = new HashMap<>();
        dummyMap.put("average", 4.5);
        dummyMap.put("reviews", java.util.List.of());

        when(reviewService.getReviewsAndSummaryByQuizId(quizId)).thenReturn(dummyMap);

        ResponseEntity<Map<String, Object>> response = reviewRestController.getReviewsForQuiz(quizId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dummyMap, response.getBody());
        verify(reviewService, times(1)).getReviewsAndSummaryByQuizId(quizId);
    }

    @Test
    void getReviewById_success() {
        Long reviewId = 5L;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(reviewId);

        when(reviewService.getReviewById(reviewId)).thenReturn(reviewDTO);

        ResponseEntity<ReviewDTO> response = reviewRestController.getReviewById(reviewId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reviewDTO, response.getBody());
        verify(reviewService, times(1)).getReviewById(reviewId);
    }

    @Test
    void updateReview_success() {
        Long reviewId = 7L;
        CreateReviewDTO updateDTO = new CreateReviewDTO();
        String nickname = "student1";
        ReviewDTO updatedReview = new ReviewDTO();
        updatedReview.setId(reviewId);

        when(reviewService.updateReview(reviewId, updateDTO, nickname)).thenReturn(updatedReview);

        ResponseEntity<ReviewDTO> response = reviewRestController.updateReview(reviewId, updateDTO, nickname);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedReview, response.getBody());
        verify(reviewService, times(1)).updateReview(reviewId, updateDTO, nickname);
    }

    @Test
    void deleteReview_success() {
        Long reviewId = 8L;
        String nickname = "student2";

        doNothing().when(reviewService).deleteReview(reviewId, nickname);

        ResponseEntity<Void> response = reviewRestController.deleteReview(reviewId, nickname);

        assertEquals(204, response.getStatusCodeValue());
        verify(reviewService, times(1)).deleteReview(reviewId, nickname);
    }
}