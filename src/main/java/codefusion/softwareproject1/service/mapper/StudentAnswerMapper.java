package codefusion.softwareproject1.service.mapper;

import codefusion.softwareproject1.dto.StudentAnswerDTO;
import codefusion.softwareproject1.entity.StudentAnswer;

import org.springframework.stereotype.Component;

@Component 
public class StudentAnswerMapper implements EntityMapper<StudentAnswer, StudentAnswerDTO> {

    @Override
    public StudentAnswerDTO toDto(StudentAnswer entity) {
        if (entity == null) {
            return null;
        }

        StudentAnswerDTO dto = new StudentAnswerDTO();
        dto.setId(entity.getId());
        dto.setCorrect(entity.isCorrect());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getAttempt() != null) {
            dto.setAttemptId(entity.getAttempt().getId());
        }

        if (entity.getQuestion() != null) {
            dto.setQuestionId(entity.getQuestion().getId());
            dto.setQuestionText(entity.getQuestion().getQuestionText()); 
        }

        if (entity.getChosenAnswer() != null) {
            dto.setChosenAnswerId(entity.getChosenAnswer().getId());
            dto.setChosenAnswerText(entity.getChosenAnswer().getAnswerText()); 
        }

        return dto;
    }

    @Override
    public StudentAnswer toEntity(StudentAnswerDTO dto) {

        if (dto == null) {
            return null;
        }
        StudentAnswer entity = new StudentAnswer();
        entity.setId(dto.getId()); 
        entity.setCorrect(dto.isCorrect());

        return entity;
    }

    @Override
    public StudentAnswer updateEntityFromDto(StudentAnswerDTO dto, StudentAnswer entity) {
       
        if (dto == null || entity == null) {
            return entity;
        }
        return entity;
    }
}