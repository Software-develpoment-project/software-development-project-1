package codefusion.softwareproject1.service;

import codefusion.softwareproject1.dto.QuizDTO;
import codefusion.softwareproject1.dto.TeacherDTO;
import codefusion.softwareproject1.entity.Teacher;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import java.util.List;

/**
 * Service interface for Teacher operations following Interface Segregation Principle.
 * Contains only methods relevant to Teacher entity operations.
 */
public interface TeacherService {
    
    /**
     * Retrieves a teacher by ID
     * 
     * @param id the teacher ID
     * @return the teacher DTO
     * @throws ResourceNotFoundException if the teacher is not found
     */
    TeacherDTO getTeacherById(Long id);
    
    /**
     * Retrieves all teachers
     * 
     * @return list of teacher DTOs
     */
    List<TeacherDTO> getAllTeachers();
    
    /**
     * Creates a new teacher
     * 
     * @param teacherDTO the teacher data transfer object
     * @return the created teacher DTO with ID field populated
     */
    TeacherDTO createTeacher(TeacherDTO teacherDTO);
    
    /**
     * Updates an existing teacher
     * 
     * @param id the teacher ID
     * @param teacherDTO the teacher data transfer object with updated fields
     * @return the updated teacher DTO
     * @throws ResourceNotFoundException if the teacher is not found
     */
    TeacherDTO updateTeacher(Long id, TeacherDTO teacherDTO);
    
    /**
     * Deletes a teacher by ID
     * 
     * @param id the teacher ID
     * @throws ResourceNotFoundException if the teacher is not found
     */
    void deleteTeacher(Long id);
    
    /**
     * Gets quizzes created by a specific teacher
     * 
     * @param id the teacher ID
     * @return list of quiz DTOs
     * @throws ResourceNotFoundException if the teacher is not found
     */
    List<QuizDTO> getQuizzesByTeacher(Long id);
}
