package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.dto.TeacherDTO;
import codefusion.softwareproject1.entity.Teacher;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.TeacherRepo;
import codefusion.softwareproject1.service.TeacherService;
import codefusion.softwareproject1.service.mapper.QuizMapper;
import codefusion.softwareproject1.service.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of TeacherService interface.
 * Follows:
 * - Single Responsibility Principle: focuses only on teacher operations
 * - Open/Closed Principle: can be extended without modification
 * - Dependency Inversion Principle: depends on abstractions (interfaces)
 */
@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepo teacherRepository;
    private final QuizRepo quizRepository;
    private final TeacherMapper teacherMapper;
    private final QuizMapper quizMapper;
    
    @Autowired
    public TeacherServiceImpl(
            TeacherRepo teacherRepository,
            QuizRepo quizRepository,
            TeacherMapper teacherMapper,
            QuizMapper quizMapper) {
        this.teacherRepository = teacherRepository;
        this.quizRepository = quizRepository;
        this.teacherMapper = teacherMapper;
        this.quizMapper = quizMapper;
    }
    
    @Override
    public TeacherDTO getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        return teacherMapper.toDto(teacher);
    }
    
    @Override
    public List<TeacherDTO> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.toEntity(teacherDTO);
        teacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(teacher);
    }
    
    @Override
    @Transactional
    public TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
        
        teacher = teacherMapper.updateEntity(teacher, teacherDTO);
        teacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(teacher);
    }
    
    @Override
    @Transactional
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Teacher", "id", id);
        }
        teacherRepository.deleteById(id);
    }
    
    @Override
    public List<QuizDTO> getQuizzesByTeacher(Long id) {
        // Verify teacher exists
        if (!teacherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Teacher", "id", id);
        }
        
        return quizRepository.findByTeacherId(id).stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
    }
} 