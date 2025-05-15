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
import org.springframework.stereotype.Service;

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
        }  catch (Exception e) {
            logger.error("Error adding question to quiz ID {}: {}", questionDTO.getQuizId(), e.getMessage());
            throw e;
        }
    }

   
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

   
    public QuestionDTO getQuestionById(Long id) {
        logger.info("Retrieving question ID: {}", id);
        
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        
        return questionMapper.toDto(question);
    }

   
    public void deleteQuestion(Long id) {
        logger.info("Deleting question ID: {}", id);
        
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", "id", id);
        }
        
        try {
            questionRepository.deleteById(id);
            logger.info("Question deleted successfully: {}", id);
        }  catch (Exception e) {
            logger.error("Error deleting question ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
    
    
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        logger.info("Updating question ID: {}", id);
        
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        
        try {
            // Update question fields from DTO
            questionMapper.updateEntityFromDto(questionDTO, existingQuestion);
            
            // If quiz ID has changed, update the quiz relationship
            if (questionDTO.getQuizId() != null && 
                (existingQuestion.getQuiz() == null || 
                !questionDTO.getQuizId().equals(existingQuestion.getQuiz().getId()))) {
                
                Quiz newQuiz = quizRepository.findById(questionDTO.getQuizId())
                        .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", questionDTO.getQuizId()));
                
                existingQuestion.setQuiz(newQuiz);
            }
            
            // Save updated question
            existingQuestion = questionRepository.save(existingQuestion);
            logger.info("Question updated successfully: {}", existingQuestion.getId());
            
            return questionMapper.toDto(existingQuestion);
        } catch (Exception e) {
            logger.error("Error updating question ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}