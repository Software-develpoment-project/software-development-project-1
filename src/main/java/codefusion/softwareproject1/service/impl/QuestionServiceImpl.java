package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.dto.QuestionDTO;
import codefusion.softwareproject1.entity.Question;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.service.QuestionService;
import codefusion.softwareproject1.service.mapper.QuestionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of QuestionService for question operations.
 * Added optimistic locking and retry capabilities.
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);
    
    private final QuestionRepo questionRepository;
    private final QuizRepo quizRepository;
    private final QuestionMapper questionMapper;

    @Autowired
    public QuestionServiceImpl(QuestionRepo questionRepository, 
                              QuizRepo quizRepository, 
                              QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    @Transactional
    @Retryable(
        value = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public QuestionDTO addQuestion(QuestionDTO questionDTO) {
        logger.info("Adding new question to quiz ID: {}", questionDTO.getQuizId());
        
        try {
            // Find the quiz
            Quiz quiz = quizRepository.findById(questionDTO.getQuizId())
                    .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", questionDTO.getQuizId()));
            
            // Convert DTO to entity
            Question question = questionMapper.toEntity(questionDTO);
            question.setQuiz(quiz);
            
            // Save question
            question = questionRepository.save(question);
            logger.info("Question added successfully with ID: {}", question.getId());
            
            return questionMapper.toDto(question);
        } catch (OptimisticLockingFailureException e) {
            logger.warn("Optimistic locking failure while adding question to quiz ID: {}. Will retry operation.", 
                      questionDTO.getQuizId());
            throw e;  // Let the @Retryable handle this
        } catch (Exception e) {
            logger.error("Error adding question to quiz ID {}: {}", questionDTO.getQuizId(), e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByQuizId(Long quizId) {
        logger.info("Retrieving questions for quiz ID: {}", quizId);
        
        // Check if quiz exists
        if (!quizRepository.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz", "id", quizId);
        }
        
        return questionRepository.findByQuizId(quizId).stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionDTO getQuestionById(Long id) {
        logger.info("Retrieving question ID: {}", id);
        
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        
        return questionMapper.toDto(question);
    }

    @Override
    @Transactional
    @Retryable(
        value = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public void deleteQuestion(Long id) {
        logger.info("Deleting question ID: {}", id);
        
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", "id", id);
        }
        
        try {
            questionRepository.deleteById(id);
            logger.info("Question deleted successfully: {}", id);
        } catch (OptimisticLockingFailureException e) {
            logger.warn("Optimistic locking failure while deleting question ID: {}. Will retry operation.", id);
            throw e;  // Let the @Retryable handle this
        } catch (Exception e) {
            logger.error("Error deleting question ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}