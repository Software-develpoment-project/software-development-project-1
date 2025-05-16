package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.dto.CreateReviewDTO;
import codefusion.softwareproject1.dto.ReviewDTO;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.entity.Review;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.exception.UnauthorizedActionException;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.ReviewRepo;
import codefusion.softwareproject1.service.ReviewService;
import codefusion.softwareproject1.service.mapper.ReviewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepo reviewRepository;
    private final QuizRepo quizRepository;     // Ensure this is injected if needed (e.g. for quiz.isPublished check)
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepo reviewRepository, QuizRepo quizRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.quizRepository = quizRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    @Transactional
    public ReviewDTO createReview(CreateReviewDTO createReviewDTO) {
        Quiz quiz = quizRepository.findById(createReviewDTO.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", createReviewDTO.getQuizId()));

        if (!quiz.isPublished()) {
            throw new UnauthorizedActionException("Cannot review a non-published quiz.");
        }

        Review review = reviewMapper.createDtoToEntity(createReviewDTO); // Call to mapper
        Review savedReview = reviewRepository.save(review); // Call to repository
        return reviewMapper.toDto(savedReview); // Call to mapper
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByQuizId(Long quizId) {
        if (!quizRepository.existsById(quizId)) { // Check if quiz exists
            throw new ResourceNotFoundException("Quiz", "id", quizId);
        }
        return reviewRepository.findByQuizIdOrderByCreatedAtDesc(quizId).stream() // Call to repository
                .map(reviewMapper::toDto) // Method reference for mapper
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getReviewsAndSummaryByQuizId(Long quizId) {
        if (!quizRepository.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz", "id", quizId);
        }
        List<Review> reviews = reviewRepository.findByQuizIdOrderByCreatedAtDesc(quizId); // Call to repository
        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(reviewMapper::toDto) // Method reference for mapper
                .collect(Collectors.toList());

        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        
        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviewDTOs);
        response.put("totalReviews", reviewDTOs.size());
        response.put("averageRating", Math.round(averageRating * 10.0) / 10.0);
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId) // Call to repository
            .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        return reviewMapper.toDto(review); // Call to mapper
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long reviewId, CreateReviewDTO createReviewDTO, String studentNickname) {
        Review review = reviewRepository.findById(reviewId) // Call to repository
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        if (studentNickname == null || !review.getStudentNickname().equalsIgnoreCase(studentNickname)) {
            throw new UnauthorizedActionException("You can only edit your own reviews.");
        }
        if (review.getQuiz() != null && !review.getQuiz().isPublished()){
            throw new UnauthorizedActionException("Cannot edit a review for a quiz that is no longer published.");
        }

        reviewMapper.updateEntityFromCreateDto(createReviewDTO, review); // Call to mapper
        Review updatedReview = reviewRepository.save(review); // Call to repository
        return reviewMapper.toDto(updatedReview); // Call to mapper
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, String studentNickname) {
        Review review = reviewRepository.findById(reviewId) // Call to repository
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));
        
        if (studentNickname == null || !review.getStudentNickname().equalsIgnoreCase(studentNickname)) {
             throw new UnauthorizedActionException("You can only delete your own reviews.");
        }
        reviewRepository.delete(review); // Call to repository
    }
}