package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.entity.Question;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.dto.QuestionDTO;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.service.QuestionService;
import codefusion.softwareproject1.service.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of QuestionService interface.
 * Follows:
 * - Single Responsibility Principle: focuses only on question operations
 * - Open/Closed Principle: can be extended without modification
 * - Dependency Inversion Principle: depends on abstractions (interfaces)
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepo questionRepository;
    private final QuizRepo quizRepository;
    private final QuestionMapper questionMapper;

    @Autowired
    public QuestionServiceImpl(
            QuestionRepo questionRepository,
            QuizRepo quizRepository,
            QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
        this.questionMapper = questionMapper;
    }

    @Override
    public QuestionDTO addQuestion(QuestionDTO questionDTO) {
        // Verify quiz exists
        Quiz quiz = quizRepository.findById(questionDTO.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", questionDTO.getQuizId()));
        
        // Convert DTO to entity
        Question question = questionMapper.toEntity(questionDTO);
        
        // Ensure quiz is set
        question.setQuiz(quiz);
        
        // Save and return mapped DTO
        question = questionRepository.save(question);
        return questionMapper.toDto(question);
    }

    @Override
    public List<QuestionDTO> getQuestionsByQuizId(Long quizId) {
        // Verify quiz exists
        if (!quizRepository.existsById(quizId)) {
            throw new ResourceNotFoundException("Quiz", "id", quizId);
        }
        
        // Find questions and map to DTOs
        return questionRepository.findByQuizId(quizId).stream()
                .map(questionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDTO getQuestionById(Long id) {
        // Find question by ID and map to DTO
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        
        return questionMapper.toDto(question);
    }

    @Override
    @Transactional
    public void deleteQuestion(Long id) {
        // Verify question exists
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", "id", id);
        }
        
        // Delete question (will cascade to answer options)
        questionRepository.deleteById(id);
    }
} 