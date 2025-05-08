package codefusion.softwareproject1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    
    private Long id; 

    @NotBlank(message = "Category title is required")
    private String title; 

    private String description;
    
    private Long teacherId;
    
    @Builder.Default
    private List<Long> quizIds = new ArrayList<>();
}