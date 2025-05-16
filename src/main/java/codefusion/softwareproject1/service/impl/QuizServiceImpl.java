package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.entity.AnswerOption;
import codefusion.softwareproject1.entity.Question;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.entity.QuizReview;
import codefusion.softwareproject1.entity.Student;
import codefusion.softwareproject1.dto.QuizAnswerSubmissionDTO;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.dto.QuizResultDTO;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.AnswerOptionRepo;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.QuizReviewRepo;
import codefusion.softwareproject1.repo.StudentRepo;
import codefusion.softwareproject1.service.QuizService;
import codefusion.softwareproject1.service.mapper.QuizMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class QuizServiceImpl implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    @Autowired
    private  QuizRepo quizRepository;

    @Autowired
    private  QuizMapper quizMapper;

    @Autowired
    private QuizReviewRepo quizReviewRepo;

    @Autowired
    private AnswerOptionRepo answerOptionRepo;

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired 
    private StudentRepo studentRepo;

    

    @Override
   
    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    
    public QuizDTO getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        return quizMapper.toDto(quiz);
    }

    @Override
    
    public QuizDTO createQuiz(QuizDTO quizDTO) {
        logger.info("Creating new quiz: {}", quizDTO.getTitle());
        Quiz quiz = quizMapper.toEntity(quizDTO);
        logger.info("Quiz entity created: {}", quiz);

        quiz = quizRepository.save(quiz);
        logger.info("Quiz created successfully with ID: {}", quiz.getId());
        return quizMapper.toDto(quiz);
    }

    @Override
    
    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        logger.info("Updating quiz with ID: {}", id);
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        
            quizMapper.updateEntityFromDto(quizDTO, existingQuiz);
            existingQuiz = quizRepository.save(existingQuiz);
            logger.info("Quiz updated successfully: {}", existingQuiz.getId());
            return quizMapper.toDto(existingQuiz);
        
    }

    @Override
    
    public void deleteQuiz(Long id) {
        logger.info("Deleting quiz with ID: {}", id);
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quiz", "id", id);
        }
        
        try {
            quizRepository.deleteById(id);
            logger.info("Quiz deleted successfully: {}", id);
        } catch (Exception e) {
            logger.warn("Optimistic locking failure while deleting quiz ID: {}. Will retry operation.", id);
            throw e;  // Let the @Retryable handle this
        }
    }

    @Override
    
    public List<QuizDTO> getPublishedQuizzes() {
        
        return quizRepository.findByPublishedTrue().stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
        
    }

    @Override
    
    public QuizDTO publishQuiz(Long id) {
        logger.info("Publishing quiz with ID: {}", id);
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        try {
            quiz.setPublished(true);
            quiz = quizRepository.save(quiz);
            logger.info("Quiz published successfully: {}", id);
            return quizMapper.toDto(quiz);
        } catch (Exception e) {
            logger.warn("Optimistic locking failure while publishing quiz ID: {}. Will retry operation.", id);
            throw e;  // Let the @Retryable handle this
        }
    }

    @Override
    
    public QuizDTO unpublishQuiz(Long id) {
        logger.info("Unpublishing quiz with ID: {}", id);
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        try {
            quiz.setPublished(false);
            quiz = quizRepository.save(quiz);
            logger.info("Quiz unpublished successfully: {}", id);
            return quizMapper.toDto(quiz);
        } catch (Exception e) {
            logger.warn("Optimistic locking failure while unpublishing quiz ID: {}. Will retry operation.", id);
            throw e;  // Let the @Retryable handle this
        }
    }
    public boolean checkAnswer(Long questionId, Long selectedOptionId) {
        // Get the question
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));
        
        // Get the selected answer option
        AnswerOption selectedOption = answerOptionRepo.findById(selectedOptionId)
                .orElseThrow(() -> new ResourceNotFoundException("AnswerOption", "id", selectedOptionId));
        
        // Verify the answer option belongs to the question
        if (!selectedOption.getQuestion().getId().equals(questionId)) {
            throw new IllegalArgumentException("The selected answer option does not belong to the specified question");
        }
        
        // Return whether the answer is correct
        return selectedOption.getIsCorrect() ;
    }
    
    /**
     * Process a complete quiz submission and calculate results.
     * 
     * @param submission the quiz answer submission
     * @return a DTO containing the quiz results
     * @throws ResourceNotFoundException if the quiz or student is not found
     */
    
    public QuizResultDTO processQuizSubmission(QuizAnswerSubmissionDTO submission) {
        // Get the quiz
        Quiz quiz = quizRepository.findById(submission.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", submission.getQuizId()));
        
        // Get the student
        Student student = studentRepo.findById(submission.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", submission.getStudentId()));
        
        // Initialize result DTO
        QuizResultDTO result = new QuizResultDTO(quiz.getId(), student.getId(), quiz.getTitle());
        
        // Get all questions for the quiz
        List<Question> questions = questionRepo.findByQuizId(quiz.getId());
        
        // Process each answer
        int correctCount = 0;
        int wrongCount = 0;
        
        for (Map.Entry<Long, Long> answer : submission.getAnswers().entrySet()) {
            Long questionId = answer.getKey();
            Long selectedAnswerId = answer.getValue();
            
            // Skip if question doesn't belong to this quiz
            if (questions.stream().noneMatch(q -> q.getId().equals(questionId))) {
                continue;
            }
            
            boolean isCorrect = checkAnswer(questionId, selectedAnswerId);
            result.addQuestionResult(questionId, isCorrect);
            
            // Add explanation for wrong answers
            if (!isCorrect) {
                Optional<AnswerOption> correctOption = answerOptionRepo.findByQuestionIdAndIsCorrect(questionId, true);
                correctOption.ifPresent(option -> {
                    String explanation = "The correct answer was: " + option.getText();
                    result.addExplanation(questionId, explanation);
                });
            }
        }
        
        // Create and save quiz review
        QuizReview quizReview = new QuizReview();
        quizReview.setQuiz(quiz);
        quizReview.setStudent(student);
        quizReview.setRightAnswers(result.getCorrectAnswers());
        quizReview.setWrongAnswers(result.getWrongAnswers());
        quizReviewRepo.save(quizReview);
        
        return result;
    }

    
}