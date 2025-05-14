package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.service.QuizService;
import codefusion.softwareproject1.service.mapper.QuizMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of QuizService for quiz operations.
 * Follows:
 * - Single Responsibility Principle: focuses only on quiz operations
 * - Open/Closed Principle: can be extended without modification
 * - Dependency Inversion Principle: depends on abstractions
 * - Added optimistic locking handling with retry mechanism
 */
@Service
public class QuizServiceImpl implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);
    private final QuizRepo quizRepository;
    private final QuizMapper quizMapper;

    @Autowired
    public QuizServiceImpl(QuizRepo quizRepository, QuizMapper quizMapper) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuizDTO getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        return quizMapper.toDto(quiz);
    }

    @Override
    @Transactional
    @Retryable(
        value = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public QuizDTO createQuiz(QuizDTO quizDTO) {
        logger.info("Creating new quiz: {}", quizDTO.getTitle());
        Quiz quiz = quizMapper.toEntity(quizDTO);
        quiz = quizRepository.save(quiz);
        logger.info("Quiz created successfully with ID: {}", quiz.getId());
        return quizMapper.toDto(quiz);
    }

    @Override
    @Transactional
    @Retryable(
        value = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        logger.info("Updating quiz with ID: {}", id);
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        try {
            quizMapper.updateEntityFromDto(quizDTO, existingQuiz);
            existingQuiz = quizRepository.save(existingQuiz);
            logger.info("Quiz updated successfully: {}", existingQuiz.getId());
            return quizMapper.toDto(existingQuiz);
        } catch ( OptimisticLockingFailureException e) {
            logger.warn("Optimistic locking failure for quiz ID: {}. Will retry operation.", id);
            throw e;  // Let the @Retryable handle this
        }
    }

    @Override
    @Transactional
    @Retryable(
        value = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public void deleteQuiz(Long id) {
        logger.info("Deleting quiz with ID: {}", id);
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quiz", "id", id);
        }
        
        try {
            quizRepository.deleteById(id);
            logger.info("Quiz deleted successfully: {}", id);
        } catch (OptimisticLockingFailureException e) {
            logger.warn("Optimistic locking failure while deleting quiz ID: {}. Will retry operation.", id);
            throw e;  // Let the @Retryable handle this
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizDTO> getPublishedQuizzes() {
        return quizRepository.findByPublishedTrue().stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @Retryable(
        value = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public QuizDTO publishQuiz(Long id) {
        logger.info("Publishing quiz with ID: {}", id);
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        try {
            quiz.setPublished(true);
            quiz = quizRepository.save(quiz);
            logger.info("Quiz published successfully: {}", id);
            return quizMapper.toDto(quiz);
        } catch (OptimisticLockingFailureException e) {
            logger.warn("Optimistic locking failure while publishing quiz ID: {}. Will retry operation.", id);
            throw e;  // Let the @Retryable handle this
        }
    }

    @Override
    @Transactional
    @Retryable(
        value = {ObjectOptimisticLockingFailureException.class, OptimisticLockingFailureException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public QuizDTO unpublishQuiz(Long id) {
        logger.info("Unpublishing quiz with ID: {}", id);
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        try {
            quiz.setPublished(false);
            quiz = quizRepository.save(quiz);
            logger.info("Quiz unpublished successfully: {}", id);
            return quizMapper.toDto(quiz);
        } catch (OptimisticLockingFailureException e) {
            logger.warn("Optimistic locking failure while unpublishing quiz ID: {}. Will retry operation.", id);
            throw e;  // Let the @Retryable handle this
        }
    }
}