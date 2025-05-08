package codefusion.softwareproject1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codefusion.softwareproject1.dto.StudentDto;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.entity.QuizReview;
import codefusion.softwareproject1.entity.Student;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.QuizReviewRepo;
import codefusion.softwareproject1.repo.StudentRepo;

@Service
public class StudentService {
    @Autowired
    private StudentRepo studentRepo;

    @Autowired 
    private QuizRepo quizRepo;

    @Autowired 
    private QuizReviewRepo quizReviewRepo;

    /**
     * Get all quizzes for a student
     * 
     * @param studentId The ID of the student
     * @return StudentDto containing student data with quiz reviews
     * @throws ResourceNotFoundException if student is not found
     */
    public StudentDto getAllQuiz(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        List<QuizReview> quizReviews = quizReviewRepo.findByStudentId(studentId);
        // Even if the list is empty, we can still return the student with an empty list
        
        return convertToStudentDto(student, quizReviews);
    }

    /**
     * Save a quiz review for a student
     * 
     * @param studentDto The student DTO containing quiz review data
     * @return The saved QuizReview entity
     * @throws ResourceNotFoundException if student or quiz is not found
     * @throws IllegalArgumentException if invalid data is provided
     */
    public QuizReview saveQuizReview(StudentDto studentDto) {
        if (studentDto == null || studentDto.getId() == null) {
            throw new IllegalArgumentException("Student data cannot be null");
        }
        
        if (studentDto.getQuizReviews() == null || studentDto.getQuizReviews().isEmpty()) {
            throw new IllegalArgumentException("Quiz review data cannot be null or empty");
        }
        
        // Convert DTO to QuizReview entity
        QuizReview quizReview = convertToQuizReviewEntity(studentDto);
        
        // Save the QuizReview entity
        return quizReviewRepo.save(quizReview);
    }

    /**
     * Convert Student entity to StudentDto
     * 
     * @param student The student entity
     * @param quizReviews List of quiz reviews for the student
     * @return StudentDto representation
     */
    public StudentDto convertToStudentDto(Student student, List<QuizReview> quizReviews) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setName(student.getName());
        studentDto.setEmail(student.getEmail());

        if (quizReviews != null) {
            studentDto.setQuizReviews(quizReviews);
        }
        
        return studentDto;
    } 

    /**
     * Convert StudentDto to QuizReview entity
     * 
     * @param studentDto The student DTO containing quiz review data
     * @return QuizReview entity
     * @throws ResourceNotFoundException if student or quiz is not found
     */
    public QuizReview convertToQuizReviewEntity(StudentDto studentDto) {
        QuizReview quizReview = new QuizReview();
        
        // Get the student entity by ID
        Student student = studentRepo.findById(studentDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentDto.getId()));
        
        quizReview.setStudent(student);
        
        // Get the quiz entity by ID
        Long quizId = studentDto.getQuizReviews().get(0).getQuiz().getId();
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));
        
        quizReview.setQuiz(quiz);
        quizReview.setRightAnswers(studentDto.getQuizReviews().get(0).getRightAnswers());
        quizReview.setWrongAnswers(studentDto.getQuizReviews().get(0).getWrongAnswers());

        return quizReview;
    }
}