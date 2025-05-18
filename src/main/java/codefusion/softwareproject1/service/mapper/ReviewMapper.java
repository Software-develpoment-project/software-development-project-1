package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.dto.CreateReviewDTO;
import codefusion.softwareproject1.dto.ReviewDTO;
import codefusion.softwareproject1.entity.Review;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.exception.ResourceNotFoundException; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final QuizRepo quizRepository;

    @Autowired
    public ReviewMapper(QuizRepo quizRepository) {
        this.quizRepository = quizRepository;
    }

    public ReviewDTO toDto(Review entity) {
        if (entity == null) {
            return null;
        }
        ReviewDTO dto = new ReviewDTO();
        dto.setId(entity.getId());
        dto.setStudentNickname(entity.getStudentNickname());
        dto.setRating(entity.getRating());
        dto.setReviewText(entity.getReviewText());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getQuiz() != null) {
            dto.setQuizId(entity.getQuiz().getId());
            dto.setQuizTitle(entity.getQuiz().getTitle()); 
        }
        return dto;
    }


    public Review createDtoToEntity(CreateReviewDTO dto) {
        if (dto == null) {
            return null;
        }
        Review entity = new Review();
        entity.setStudentNickname(dto.getStudentNickname());
        entity.setRating(dto.getRating());
        entity.setReviewText(dto.getReviewText());

        if (dto.getQuizId() != null) {
            Quiz quiz = quizRepository.findById(dto.getQuizId())
                    .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", dto.getQuizId()));
            entity.setQuiz(quiz);
        } else {
            throw new IllegalArgumentException("Quiz ID must be provided when creating a review.");
        }
        return entity;
    }


    public void updateEntityFromCreateDto(CreateReviewDTO dto, Review entity) {
        if (dto == null || entity == null) {
            return;
        }
       
        entity.setRating(dto.getRating());
        entity.setReviewText(dto.getReviewText());
       
    }
}