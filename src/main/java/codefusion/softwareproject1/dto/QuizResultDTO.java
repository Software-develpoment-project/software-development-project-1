package codefusion.softwareproject1.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO for quiz results.
 * Contains data about the correctness of each answer and overall quiz statistics.
 */
public class QuizResultDTO {
    
    private Long quizId;
    private Long studentId;
    private String quizTitle;
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    
    // Map of questionId -> boolean indicating if answer was correct
    private Map<Long, Boolean> questionResults = new HashMap<>();
    
    // Map of questionId -> String with explanation of correct answer (optional)
    private Map<Long, String> explanations = new HashMap<>();
    
    // Default constructor
    public QuizResultDTO() {
    }
    
    // Constructor with fields
    public QuizResultDTO(Long quizId, Long studentId, String quizTitle) {
        this.quizId = quizId;
        this.studentId = studentId;
        this.quizTitle = quizTitle;
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
    
    public String getQuizTitle() {
        return quizTitle;
    }
    
    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }
    
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    
    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
    
    public int getWrongAnswers() {
        return wrongAnswers;
    }
    
    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
    
    public Map<Long, Boolean> getQuestionResults() {
        return questionResults;
    }
    
    public void setQuestionResults(Map<Long, Boolean> questionResults) {
        this.questionResults = questionResults;
    }
    
    public Map<Long, String> getExplanations() {
        return explanations;
    }
    
    public void setExplanations(Map<Long, String> explanations) {
        this.explanations = explanations;
    }
    
    // Methods to add results
    public void addQuestionResult(Long questionId, boolean isCorrect) {
        this.questionResults.put(questionId, isCorrect);
        if (isCorrect) {
            this.correctAnswers++;
        } else {
            this.wrongAnswers++;
        }
        this.totalQuestions = this.correctAnswers + this.wrongAnswers;
    }
    
    public void addExplanation(Long questionId, String explanation) {
        this.explanations.put(questionId, explanation);
    }
}