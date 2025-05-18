package codefusion.softwareproject1.service;

import codefusion.softwareproject1.dto.AnswerSubmissionDTO;
import codefusion.softwareproject1.dto.QuizAttemptDTO;
import codefusion.softwareproject1.dto.QuizResultDTO;
import codefusion.softwareproject1.entity.StudentAnswer;



public interface QuizAttemptService {

    QuizAttemptDTO startQuizAttempt(Long quizId, Long studentId);

    StudentAnswer submitStudentAnswer(Long attemptId, Long questionId, AnswerSubmissionDTO submissionDTO, Long studentId);

    QuizResultDTO getQuizResults(Long quizId);
}
