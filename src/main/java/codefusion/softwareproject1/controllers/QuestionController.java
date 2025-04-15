package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.DTO.ChoiceDTO;
import codefusion.softwareproject1.DTO.QuestionsDTO;
import codefusion.softwareproject1.Models.ChoiceClass;
import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.QuestionsClass;
import codefusion.softwareproject1.Models.QuestionsClass.DifficultyLevel;
import codefusion.softwareproject1.repo.ChoiceRepo;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.QuestionsRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionsRepo questionRepository;

    @Autowired
    private QuizRepo quizRepository;
    
    @Autowired
    private ChoiceRepo choiceRepository;

    // Get questions by quiz
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuestionsDTO>> getQuestionsByQuiz(@PathVariable Long quizId) {
        List<QuestionsClass> questions = questionRepository.findByQuizId(quizId);
        List<QuestionsDTO> questionDTOs = questions.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(questionDTOs);
    }
    
    // Get questions by difficulty
    @GetMapping("/difficulty/{level}")
    public ResponseEntity<List<QuestionsDTO>> getQuestionsByDifficulty(
            @PathVariable("level") DifficultyLevel difficultyLevel) {
        List<QuestionsClass> questions = questionRepository.findByDifficultyLevel(difficultyLevel);
        List<QuestionsDTO> questionDTOs = questions.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(questionDTOs);
    }

    // Add question to quiz
    @PostMapping("/quiz/{quizId}")
    public ResponseEntity<QuestionsDTO> addQuestionToQuiz(
            @PathVariable Long quizId, 
            @RequestBody QuestionsDTO questionDTO) {
        
        return quizRepository.findById(quizId)
                .map(quiz -> {
                    QuestionsClass question = convertToEntity(questionDTO, quiz);
                    QuestionsClass savedQuestion = questionRepository.save(question);
                    
                    // Save choices
                    if (questionDTO.getChoices() != null) {
                        questionDTO.getChoices().forEach(choiceDTO -> {
                            ChoiceClass choice = new ChoiceClass();
                            choice.setContent(choiceDTO.getContent());
                            choice.setCorrect(choiceDTO.isCorrect());
                            choice.setQuestion(savedQuestion);
                            choiceRepository.save(choice);
                        });
                    }
                    
                    return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedQuestion));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get all questions
    @GetMapping
    public ResponseEntity<List<QuestionsDTO>> getAllQuestions() {
        List<QuestionsClass> questions = questionRepository.findAll();
        List<QuestionsDTO> questionDTOs = questions.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(questionDTOs);
    }

    // Get question by id
    @GetMapping("/{id}")
    public ResponseEntity<QuestionsDTO> getQuestionById(@PathVariable Long id) {
        return questionRepository.findById(id)
                .map(question -> ResponseEntity.ok(convertToDTO(question)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update question
    @PutMapping("/{id}")
    public ResponseEntity<QuestionsDTO> updateQuestion(
            @PathVariable Long id, 
            @RequestBody QuestionsDTO questionDTO) {
        
        return questionRepository.findById(id)
                .map(question -> {
                    // Update question fields
                    question.setContent(questionDTO.getContent());
                    question.setDifficultyLevel(questionDTO.getDifficultyLevel());
                    
                    // Save updated question
                    QuestionsClass updatedQuestion = questionRepository.save(question);
                    
                    // Update choices if provided
                    if (questionDTO.getChoices() != null) {
                        // Remove existing choices
                        choiceRepository.deleteAll(question.getChoices());
                        
                        // Add new choices
                        List<ChoiceClass> newChoices = new ArrayList<>();
                        questionDTO.getChoices().forEach(choiceDTO -> {
                            ChoiceClass choice = new ChoiceClass();
                            choice.setContent(choiceDTO.getContent());
                            choice.setCorrect(choiceDTO.isCorrect());
                            choice.setQuestion(updatedQuestion);
                            newChoices.add(choiceRepository.save(choice));
                        });
                        
                        updatedQuestion.setChoices(newChoices);
                    }
                    
                    return ResponseEntity.ok(convertToDTO(updatedQuestion));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete question
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        return questionRepository.findById(id)
                .map(question -> {
                    questionRepository.deleteById(id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    
    // Helper methods
    private QuestionsDTO convertToDTO(QuestionsClass question) {
        QuestionsDTO dto = new QuestionsDTO();
        dto.setId(question.getId());
        dto.setContent(question.getContent());
        dto.setDifficultyLevel(question.getDifficultyLevel());
        dto.setQuizId(question.getQuiz() != null ? question.getQuiz().getId() : null);
        
        if (question.getChoices() != null) {
            List<ChoiceDTO> choiceDTOs = question.getChoices().stream()
                .map(choice -> {
                    ChoiceDTO choiceDTO = new ChoiceDTO();
                    choiceDTO.setId(choice.getId());
                    choiceDTO.setContent(choice.getOptionText());
                    choiceDTO.setCorrect(choice.isCorrect());
                    choiceDTO.setQuestionId(question.getId());
                    return choiceDTO;
                })
                .collect(Collectors.toList());
            dto.setChoices(choiceDTOs);
        }
        
        return dto;
    }
    
    private QuestionsClass convertToEntity(QuestionsDTO dto, QuizClass quiz) {
        QuestionsClass question = new QuestionsClass();
        question.setContent(dto.getContent());
        question.setDifficultyLevel(dto.getDifficultyLevel());
        question.setQuiz(quiz);
        return question;
    }
}