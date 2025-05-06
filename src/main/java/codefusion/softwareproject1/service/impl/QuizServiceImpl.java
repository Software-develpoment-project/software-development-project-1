package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.entity.Category;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.CategoryRepo;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.service.CategoryService;
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
    private final CategoryService categoryService;
    private final CategoryRepo categoryRepo;

    @Autowired
    public QuizServiceImpl(QuizRepo quizRepository, QuizMapper quizMapper,CategoryService categoryService,CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
        this.categoryService = categoryService;
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

    @Transactional
    public QuizDTO createQuiz(QuizDTO quizDTO) {
        // Convert DTO to entity
        Quiz quiz = quizMapper.toEntity(quizDTO);
        
        // Save the quiz first to generate ID
        quiz = quizRepository.save(quiz);
        
        // Associate quiz with categories if categoryIds are provided
        if (quizDTO.getCategoryIds() != null && !quizDTO.getCategoryIds().isEmpty()) {
            for (Long categoryId : quizDTO.getCategoryIds()) {
                categoryService.addQuizToCategory(categoryId, quiz.getId());
            }
        }
        
        // Get the updated quiz with associations
        if (!quizRepository.findById((quiz.getId())).isPresent()) {
            return null ;
        }
        quiz = quizRepository.findById(quiz.getId()).orElse(quiz);
        
        return quizMapper.toDto(quiz);
    }
    
    /**
     * Update an existing quiz
     */
    @Transactional
    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        // Update basic quiz properties
        quizMapper.updateEntityFromDto(quizDTO, existingQuiz);
        existingQuiz = quizRepository.save(existingQuiz);
        
        // Handle category associations if provided
        if (quizDTO.getCategoryIds() != null) {
            // Get current categories for this quiz
            List<Category> currentCategories = categoryRepo.findByQuizzesId(id) ;
            
            // Remove quiz from categories that are no longer associated
            for (Category category : currentCategories) {
                if (!quizDTO.getCategoryIds().contains(category.getId())) {
                    categoryService.removeQuizFromCategory(category.getId(), id);
                }
            }
            
            // Add quiz to new categories
            for (Long categoryId : quizDTO.getCategoryIds()) {
                boolean exists = currentCategories.stream()
                        .anyMatch(cat -> cat.getId().equals(categoryId));
                if (!exists) {
                    categoryService.addQuizToCategory(categoryId, id);
                }
            }
        }
        
        // Get the updated quiz with associations
        existingQuiz = quizRepository.findById(id).orElse(existingQuiz);
        
        return quizMapper.toDto(existingQuiz);
    }
    
    /**
     * Delete a quiz
     */
    @Transactional
    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quiz", "id", id);
        }
        
        // Remove quiz from all categories first
        List<Category> categories = categoryService.getCategoriesByQuiz(id);
        for (Category category : categories) {
            categoryService.removeQuizFromCategory(category.getId(), id);
        }
        
        // Delete the quiz
        quizRepository.deleteById(id);
    }
    
    /**
     * Get all published quizzes
     */
    public List<QuizDTO> getPublishedQuizzes() {
        return quizRepository.findByPublishedTrue().stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get quizzes by teacher ID
     */
    public List<QuizDTO> getQuizzesByTeacher(Long teacherId) {
        return quizRepository.findByTeacherId(teacherId).stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Publish a quiz
     */
    @Transactional
    public QuizDTO publishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        quiz.setPublished(true);
        quiz = quizRepository.save(quiz);
        
        return quizMapper.toDto(quiz);
    }
    
    /**
     * Unpublish a quiz
     */
    @Transactional
    public QuizDTO unpublishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        
        quiz.setPublished(false);
        quiz = quizRepository.save(quiz);
        
        return quizMapper.toDto(quiz);
    }
} 