package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.entity.Category;
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.dto.CategoryDTO;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.CategoryRepo;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.service.CategoryService;
import codefusion.softwareproject1.service.QuizService;
import codefusion.softwareproject1.service.mapper.QuizMapper;
import codefusion.softwareproject1.service.mapper.CategoryMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of QuizService for quiz operations.
 */
@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepo quizRepository;
    private final QuizMapper quizMapper;
    private final CategoryService categoryService;
    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    public QuizServiceImpl(
            QuizRepo quizRepository,
            QuizMapper quizMapper,
            CategoryService categoryService,
            CategoryRepo categoryRepo,
            CategoryMapper categoryMapper
    ) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
        this.categoryService = categoryService;
        this.categoryRepo = categoryRepo;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<QuizDTO> getAllQuizzes() {
        return quizRepository.findAll().stream().map(quiz -> {
            QuizDTO dto = quizMapper.toDto(quiz);
            List<CategoryDTO> categoryDTOs = categoryRepo.findByQuizzesId(quiz.getId())
                    .stream().map(categoryMapper::toDto).collect(Collectors.toList());
            dto.setCategories(categoryDTOs);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public QuizDTO getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));

        QuizDTO dto = quizMapper.toDto(quiz);
        List<CategoryDTO> categoryDTOs = categoryRepo.findByQuizzesId(id)
                .stream().map(categoryMapper::toDto).collect(Collectors.toList());
        dto.setCategories(categoryDTOs);

        return dto;
    }

    @Override
    @Transactional
    public QuizDTO createQuiz(QuizDTO quizDTO) {
        Quiz quiz = quizMapper.toEntity(quizDTO);
        Quiz savedQuiz = quizRepository.save(quiz);

        if (quizDTO.getCategoryIds() != null && !quizDTO.getCategoryIds().isEmpty()) {
            for (Long categoryId : quizDTO.getCategoryIds()) {
                categoryService.addQuizToCategory(categoryId, savedQuiz.getId());
            }
        }

        List<CategoryDTO> categoryDTOs = categoryRepo.findByQuizzesId(savedQuiz.getId())
                .stream().map(categoryMapper::toDto).collect(Collectors.toList());
        QuizDTO resultDTO = quizMapper.toDto(savedQuiz);
        resultDTO.setCategories(categoryDTOs);

        return resultDTO;
    }

    @Override
    @Transactional
    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        Quiz existingQuiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));

        quizMapper.updateEntityFromDto(quizDTO, existingQuiz);
        existingQuiz = quizRepository.save(existingQuiz);

        if (quizDTO.getCategoryIds() != null) {
            List<Category> currentCategories = categoryRepo.findByQuizzesId(id);

            for (Category category : currentCategories) {
                if (!quizDTO.getCategoryIds().contains(category.getId())) {
                    categoryService.removeQuizFromCategory(category.getId(), id);
                }
            }

            for (Long categoryId : quizDTO.getCategoryIds()) {
                boolean exists = currentCategories.stream()
                        .anyMatch(cat -> cat.getId().equals(categoryId));
                if (!exists) {
                    categoryService.addQuizToCategory(categoryId, id);
                }
            }
        }

        List<CategoryDTO> updatedCategoryDTOs = categoryRepo.findByQuizzesId(id)
                .stream().map(categoryMapper::toDto).collect(Collectors.toList());

        QuizDTO resultDTO = quizMapper.toDto(existingQuiz);
        resultDTO.setCategories(updatedCategoryDTOs);

        return resultDTO;
    }

    @Override
    @Transactional
    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quiz", "id", id);
        }

        List<Category> categories = categoryService.getCategoriesByQuiz(id);
        for (Category category : categories) {
            categoryService.removeQuizFromCategory(category.getId(), id);
        }

        quizRepository.deleteById(id);
    }

    @Override
    public List<QuizDTO> getPublishedQuizzes() {
        return quizRepository.findByPublishedTrue().stream().map(quiz -> {
            QuizDTO dto = quizMapper.toDto(quiz);
            List<CategoryDTO> categoryDTOs = categoryRepo.findByQuizzesId(quiz.getId())
                    .stream().map(categoryMapper::toDto).collect(Collectors.toList());
            dto.setCategories(categoryDTOs);
            return dto;
        }).collect(Collectors.toList());
    }

    
    public List<QuizDTO> getQuizzesByTeacher(Long teacherId) {
        return quizRepository.findByTeacherId(teacherId).stream().map(quiz -> {
            QuizDTO dto = quizMapper.toDto(quiz);
            List<CategoryDTO> categoryDTOs = categoryRepo.findByQuizzesId(quiz.getId())
                    .stream().map(categoryMapper::toDto).collect(Collectors.toList());
            dto.setCategories(categoryDTOs);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuizDTO publishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));

        quiz.setPublished(true);
        quiz = quizRepository.save(quiz);

        List<CategoryDTO> categoryDTOs = categoryRepo.findByQuizzesId(id)
                .stream().map(categoryMapper::toDto).collect(Collectors.toList());

        QuizDTO resultDTO = quizMapper.toDto(quiz);
        resultDTO.setCategories(categoryDTOs);

        return resultDTO;
    }

    @Override
    @Transactional
    public QuizDTO unpublishQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));

        quiz.setPublished(false);
        quiz = quizRepository.save(quiz);

        List<CategoryDTO> categoryDTOs = categoryRepo.findByQuizzesId(id)
                .stream().map(categoryMapper::toDto).collect(Collectors.toList());

        QuizDTO resultDTO = quizMapper.toDto(quiz);
        resultDTO.setCategories(categoryDTOs);

        return resultDTO;
    }
}
