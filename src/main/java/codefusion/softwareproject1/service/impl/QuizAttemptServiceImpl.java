package codefusion.softwareproject1.service.impl;

import codefusion.softwareproject1.dto.AnswerSubmissionDTO;
import codefusion.softwareproject1.dto.QuestionResultDTO;
import codefusion.softwareproject1.dto.QuizAttemptDTO;
import codefusion.softwareproject1.dto.QuizResultDTO;
import codefusion.softwareproject1.entity.*;
import codefusion.softwareproject1.exception.QuizException;
import codefusion.softwareproject1.exception.ResourceNotFoundException;
import codefusion.softwareproject1.repo.*;
import codefusion.softwareproject1.service.QuizAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuizAttemptServiceImpl implements QuizAttemptService {

    private final QuizRepo quizRepo;
    private final QuestionRepo questionRepo;
    private final AnswerOptionRepo answerOptionRepo;
    private final QuizAttemptRepo quizAttemptRepo;
    private final StudentAnswerRepo studentAnswerRepo;

    @Autowired
    public QuizAttemptServiceImpl(QuizRepo quizRepo,
                                QuestionRepo questionRepo,
                                AnswerOptionRepo answerOptionRepo,
                                QuizAttemptRepo quizAttemptRepo,
                                StudentAnswerRepo studentAnswerRepo) {
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
        this.answerOptionRepo = answerOptionRepo;
        this.quizAttemptRepo = quizAttemptRepo;
        this.studentAnswerRepo = studentAnswerRepo;
    }

    @Override
    @Transactional
    public QuizAttemptDTO startQuizAttempt(Long quizId, Long studentId) {
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        if (!quiz.isPublished()) {
            throw new QuizException("Quiz with ID " + quizId + " is not published and cannot be attempted.");
        }

        QuizAttempt newAttempt = new QuizAttempt();
        newAttempt.setQuiz(quiz);

        QuizAttempt savedAttempt = quizAttemptRepo.save(newAttempt);

        QuizAttemptDTO dto = new QuizAttemptDTO();
        dto.setId(savedAttempt.getId());
        dto.setQuizId(savedAttempt.getQuiz().getId());
        dto.setQuizTitle(savedAttempt.getQuiz().getTitle());
        dto.setAttemptDate(savedAttempt.getAttemptDate());
        dto.setScore(savedAttempt.getScore());
        return dto;
    }

    @Override
    @Transactional
    public StudentAnswer submitStudentAnswer(Long attemptId, Long questionId, AnswerSubmissionDTO submissionDTO, Long studentId) {
        if (submissionDTO.getAnswerOptionId() == null) {
            throw new IllegalArgumentException("AnswerOptionId cannot be null in submission.");
        }

        QuizAttempt attempt = quizAttemptRepo.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("QuizAttempt", "id", attemptId));

        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", questionId));

        AnswerOption chosenAnswer = answerOptionRepo.findById(submissionDTO.getAnswerOptionId())
                .orElseThrow(() -> new ResourceNotFoundException("AnswerOption", "id", submissionDTO.getAnswerOptionId()));

        if (!question.getQuiz().getId().equals(attempt.getQuiz().getId())) {
            throw new QuizException("Question with ID " + questionId + " does not belong to the quiz of attempt ID " + attemptId);
        }

        if (!chosenAnswer.getQuestion().getId().equals(questionId)) {
            throw new QuizException("Chosen AnswerOption with ID " + chosenAnswer.getId() + " does not belong to Question ID " + questionId);
        }

        StudentAnswer studentAnswer = new StudentAnswer();
        studentAnswer.setAttempt(attempt);
        studentAnswer.setQuestion(question);
        studentAnswer.setChosenAnswer(chosenAnswer);
        studentAnswer.setCorrect(chosenAnswer.getIsCorrect());

        return studentAnswerRepo.save(studentAnswer);
    }

    @Override
    @Transactional(readOnly = true)
    public QuizResultDTO getQuizResults(Long quizId) {
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", quizId));

        List<Question> questionsInQuiz = questionRepo.findByQuizId(quizId);
        if (questionsInQuiz.isEmpty()) {
            return new QuizResultDTO(quizId, quiz.getTitle(), new ArrayList<>());
        }

        List<Long> questionIdsInQuiz = questionsInQuiz.stream().map(Question::getId).collect(Collectors.toList());

        List<StudentAnswer> relevantStudentAnswers = studentAnswerRepo.findByQuestionIdIn(questionIdsInQuiz);

        Map<Long, List<StudentAnswer>> answersByQuestionId = relevantStudentAnswers.stream()
            .filter(sa -> sa.getAttempt().getQuiz().getId().equals(quizId))
            .collect(Collectors.groupingBy(sa -> sa.getQuestion().getId()));

        List<QuestionResultDTO> questionResults = new ArrayList<>();
        for (Question question : questionsInQuiz) {
            List<StudentAnswer> answersForThisQuestion = answersByQuestionId.getOrDefault(question.getId(), new ArrayList<>());

            long totalAnswers = answersForThisQuestion.size();
            long correctAnswers = answersForThisQuestion.stream().filter(StudentAnswer::isCorrect).count();
            long wrongAnswers = totalAnswers - correctAnswers;

            questionResults.add(new QuestionResultDTO(
                    question.getId(),
                    question.getQuestionText(),
                    question.getDifficultyLevel() != null ? question.getDifficultyLevel().name() : "N/A",
                    totalAnswers,
                    correctAnswers,
                    wrongAnswers
            ));
        }
        return new QuizResultDTO(quizId, quiz.getTitle(), questionResults);
    }
}