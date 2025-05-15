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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class QuizServiceImpl implements QuizService {

    private static final Logger logger = LoggerFactory.getLogger(QuizServiceImpl.class);

    @Autowired
    private  QuizRepo quizRepository;

    @Autowired
    private  QuizMapper quizMapper;

    

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
}