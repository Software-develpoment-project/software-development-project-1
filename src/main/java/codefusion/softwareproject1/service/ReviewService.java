package codefusion.softwareproject1.service;

import codefusion.softwareproject1.dto.CreateReviewDTO;
import codefusion.softwareproject1.dto.ReviewDTO;
import java.util.List;
import java.util.Map;

public interface ReviewService {
    ReviewDTO createReview(CreateReviewDTO createReviewDTO);
    List<ReviewDTO> getReviewsByQuizId(Long quizId);
    Map<String, Object> getReviewsAndSummaryByQuizId(Long quizId); // For avg rating & count
    ReviewDTO updateReview(Long reviewId, CreateReviewDTO createReviewDTO, String studentNickname); // Pass nickname for ownership check
    void deleteReview(Long reviewId, String studentNickname); // Pass nickname for ownership check
    ReviewDTO getReviewById(Long reviewId); // For fetching review to edit
}