package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.dto.TeacherDTO;
import codefusion.softwareproject1.entity.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {
    
   
    public TeacherDTO toDto(Teacher teacher) {
        if (teacher == null) {
            return null;
        }
        
        TeacherDTO dto = new TeacherDTO();
        dto.setId(teacher.getId());
        dto.setName(teacher.getName());
        dto.setEmail(teacher.getEmail());
        
        return dto;
    }
    
  
    public Teacher toEntity(TeacherDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Teacher teacher = new Teacher();
        
        if (dto.getId() != null) {
            teacher.setId(dto.getId());
        }
        
        teacher.setName(dto.getName());
        teacher.setEmail(dto.getEmail());
        
        return teacher;
    }
    
    public Teacher updateEntity(Teacher teacher, TeacherDTO dto) {
        if (teacher == null || dto == null) {
            return teacher;
        }
        
        if (dto.getName() != null) {
            teacher.setName(dto.getName());
        }
        
        if (dto.getEmail() != null) {
            teacher.setEmail(dto.getEmail());
        }
        
        return teacher;
    }
} 