package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.service.QuizService;
import codefusion.softwareproject1.service.mapper.QuizMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of QuizService for quiz operations.
 * Follows:
 * - Single Responsibility Principle: focuses only on quiz operations
 * - Open/Closed Principle: can be extended without modification
 * - Dependency Inversion Principle: depends on abstractions
 */
@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepo quizRepository;
    private final QuizMapper quizMapper;

    @Autowired
    public QuizServiceImpl(QuizRepo quizRepository, QuizMapper quizMapper) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
    }

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
        Quiz quiz = quizMapper.toEntity(quizDTO);
        quiz = quizRepository.save(quiz);
        return quizMapper.toDto(quiz);
    }

    @Override
    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        quizMapper.updateEntityFromDto(quizDTO, existingQuiz);
        existingQuiz = quizRepository.save(existingQuiz);
        
        return quizMapper.toDto(existingQuiz);
    }

    @Override
    @Transactional
    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quiz", "id", id);
        }
        
        quizRepository.deleteById(id);
    }

    @Override
    public List<QuizDTO> getPublishedQuizzes() {
        return quizRepository.findByPublishedTrue().stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuizDTO publishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        quiz.setPublished(true);
        quiz = quizRepository.save(quiz);
        
        return quizMapper.toDto(quiz);
    }

    @Override
    public QuizDTO unpublishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        quiz.setPublished(false);
        quiz = quizRepository.save(quiz);
        
        return quizMapper.toDto(quiz);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizDTO> getPublishedQuizzesByCategoryId(Long categoryId) {
        // It's good practice to ensure the category itself exists, though the query might just return empty.
        // CategoryRepo would be needed for that, or rely on the query.
        // For now, directly query quizzes by categoryId and published status.
        List<Quiz> quizzes = quizRepository.findByCategoryIdAndPublishedTrue(categoryId);
        return quizzes.stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
    }
} 