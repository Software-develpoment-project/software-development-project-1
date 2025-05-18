package codefusion.softwareproject1.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewDTO {

    @NotNull(message = "Quiz ID is required")
    private Long quizId; // Not directly in form but needed to associate the review

    @NotBlank(message = "Nickname is required")
    @Size(min = 3, max = 100, message = "Nickname must be between 3 and 100 characters")
    private String studentNickname;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Review text is required")
    @Size(min = 10, max = 2000, message = "Review text must be between 10 and 2000 characters")
    private String reviewText;
}