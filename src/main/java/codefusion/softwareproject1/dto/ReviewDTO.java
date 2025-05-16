package codefusion.softwareproject1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long quizId;
    private String quizTitle; // Denormalized for convenience
    private String studentNickname;
    private Integer rating;
    private String reviewText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}