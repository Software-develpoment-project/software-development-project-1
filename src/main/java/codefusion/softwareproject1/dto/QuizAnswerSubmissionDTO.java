package codefusion.softwareproject1.dto;

import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * DTO for quiz answer submissions.
 * Contains data for a student's quiz submission including their answers to questions.
 */
public class QuizAnswerSubmissionDTO {
    
    @NotNull(message = "Quiz ID is required")
    private Long quizId;
    
    @NotNull(message = "Student ID is required")
    private Long studentId;
    
    // Map of questionId -> selected answerOptionId
    private Map<Long, Long> answers = new HashMap<>();
    
    // Default constructor
    public QuizAnswerSubmissionDTO() {
    }
    
    // Constructor with fields
    public QuizAnswerSubmissionDTO(Long quizId, Long studentId, Map<Long, Long> answers) {
        this.quizId = quizId;
        this.studentId = studentId;
        this.answers = answers;
    }
    
    // Getters and setters
    public Long getQuizId() {
        return quizId;
    }
    
    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }
    
    public Long getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    
    public Map<Long, Long> getAnswers() {
        return answers;
    }
    
    public void setAnswers(Map<Long, Long> answers) {
        this.answers = answers;
    }
    
    // Method to add an answer
    public void addAnswer(Long questionId, Long answerOptionId) {
        this.answers.put(questionId, answerOptionId);
    }
}