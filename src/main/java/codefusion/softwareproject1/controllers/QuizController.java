package codefusion.softwareproject1.controllers;

<<<<<<< HEAD
=======
import codefusion.softwareproject1.entity.Quiz;
import codefusion.softwareproject1.entity.AnswerOption;
import codefusion.softwareproject1.entity.Question;
import codefusion.softwareproject1.entity.Teacher;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
>>>>>>> 2cd1609e75a00b9bd52d6adb7ea9907bc0b2ae5f
import java.util.List;
import java.util.Optional;

<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import codefusion.softwareproject1.Models.QuestionsClass;
import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.TeacherClass;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.TeacherRepo;


@RequestMapping("/api/quizzes")
=======
@Controller
@RequestMapping("/quizzes")
>>>>>>> 2cd1609e75a00b9bd52d6adb7ea9907bc0b2ae5f
public class QuizController {

    @Autowired
    private QuizRepo quizRepository;

    @Autowired
    private QuestionRepo questionRepository;

    @Autowired
    private TeacherRepo teacherRepository;

    // Quiz Management

    @GetMapping
    public String getAllQuizzes(Model model) {
        List<Quiz> quizzes = quizRepository.findAll();
        model.addAttribute("quizzes", quizzes);
        return "quiz-list";
    }

    @GetMapping("/new")
    public String showCreateQuizForm(Model model) {
        Quiz quiz = new Quiz();
        List<Teacher> teachers = teacherRepository.findAll();

        model.addAttribute("quiz", quiz);
        model.addAttribute("teachers", teachers);
        return "quiz-form";
    }

    @PostMapping("/save")
    public String createQuiz(@ModelAttribute Quiz quiz, RedirectAttributes redirectAttributes) {
        quizRepository.save(quiz);
        redirectAttributes.addFlashAttribute("message", "Quiz created successfully!");
        return "redirect:/quizzes";
    }

    @GetMapping("/{id}")
    public String getQuizById(@PathVariable Long id, Model model) {
        Optional<Quiz> quizOpt = quizRepository.findById(id);
        if (quizOpt.isPresent()) {
            model.addAttribute("quiz", quizOpt.get());
            return "quiz-details";
        } else {
            model.addAttribute("errorMessage", "Quiz not found!");
            return "quiz-list";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditQuizForm(@PathVariable Long id, Model model) {
        Optional<Quiz> quizOpt = quizRepository.findById(id);
        if (quizOpt.isPresent()) {
            List<Teacher> teachers = teacherRepository.findAll();
            model.addAttribute("quiz", quizOpt.get());
            model.addAttribute("teachers", teachers);
            return "quiz-form";
        } else {
            model.addAttribute("errorMessage", "Quiz not found!");
            return "redirect:/quizzes";
        }
    }

    @PostMapping("/update")
    public String updateQuiz(@ModelAttribute Quiz updatedQuiz, RedirectAttributes redirectAttributes) {
        Optional<Quiz> quizOpt = quizRepository.findById(updatedQuiz.getId());
        if (quizOpt.isPresent()) {
            Quiz quiz = quizOpt.get();
            quiz.setTitle(updatedQuiz.getTitle());
            quiz.setDescription(updatedQuiz.getDescription());
            quiz.setPublished(updatedQuiz.isPublished());
            quiz.setDifficulty(updatedQuiz.getDifficulty());
            quiz.setTopic(updatedQuiz.getTopic());
            quiz.setTeacher(updatedQuiz.getTeacher());
            
            quizRepository.save(quiz);
            redirectAttributes.addFlashAttribute("message", "Quiz updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Quiz not found!");
        }
        return "redirect:/quizzes";
    }

    @PostMapping("/{id}/delete")
    public String deleteQuiz(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Quiz> quizOpt = quizRepository.findById(id);
        if (quizOpt.isPresent()) {
            quizRepository.delete(quizOpt.get());
            redirectAttributes.addFlashAttribute("message", "Quiz deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Quiz not found!");
        }
        return "redirect:/quizzes";
    }

    // Question Management
    @GetMapping("/{quizId}/questions")
    public String getQuestionsByQuiz(@PathVariable Long quizId, Model model) {
        Optional<Quiz> quizOpt = quizRepository.findById(quizId);
        if (quizOpt.isPresent()) {
            Quiz quiz = quizOpt.get();
            List<Question> questions = questionRepository.findByQuizId(quizId);

            model.addAttribute("quiz", quiz);
            model.addAttribute("questions", questions);
            return "questions-list";
        } else {
            model.addAttribute("errorMessage", "Quiz not found!");
            return "redirect:/quizzes";
        }
    }

    @GetMapping("/{quizId}/questions/create")
    public String showCreateQuestionForm(@PathVariable Long quizId, Model model) {
        Optional<Quiz> quizOpt = quizRepository.findById(quizId);
        if (quizOpt.isPresent()) {
            Quiz quiz = quizOpt.get();
            Question question = new Question();

            // Initialize the question with the quiz
            question.setQuiz(quiz);

            // Create default empty answer options
            List<AnswerOption> answerOptions = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                AnswerOption option = new AnswerOption();
                option.setQuestion(question);
                answerOptions.add(option);
            }
            question.setAnswerOptions(answerOptions);

            model.addAttribute("quiz", quiz);
            model.addAttribute("question", question);
            model.addAttribute("difficultyLevels", Question.DifficultyLevel.values());
            return "question-form";
        } else {
            model.addAttribute("errorMessage", "Quiz not found!");
            return "redirect:/quizzes";
        }
    }

    @PostMapping("/{quizId}/questions/save")
    public String createQuestion(
            @PathVariable Long quizId,
            @ModelAttribute Question question,
            @RequestParam("correctChoiceIndex") Integer correctChoiceIndex,
            RedirectAttributes redirectAttributes) {

        Optional<Quiz> quizOpt = quizRepository.findById(quizId);
        if (quizOpt.isPresent()) {
            Quiz quiz = quizOpt.get();
            question.setQuiz(quiz);

            // Set correct answer
            if (correctChoiceIndex != null && correctChoiceIndex >= 0 &&
                    correctChoiceIndex < question.getAnswerOptions().size()) {
                for (int i = 0; i < question.getAnswerOptions().size(); i++) {
                    AnswerOption choice = question.getAnswerOptions().get(i);
                    choice.setCorrect(i == correctChoiceIndex);
                    choice.setQuestion(question);
                }
            }

            questionRepository.save(question);
            redirectAttributes.addFlashAttribute("message", "Question created successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Quiz not found!");
        }
        return "redirect:/quizzes/" + quizId + "/questions";
    }

    @GetMapping("/{quizId}/questions/{questionId}/edit")
    public String showEditQuestionForm(
            @PathVariable Long quizId,
            @PathVariable Long questionId,
            Model model) {
        Optional<Quiz> quizOpt = quizRepository.findById(quizId);
        Optional<Question> questionOpt = questionRepository.findById(questionId);

        if (quizOpt.isPresent() && questionOpt.isPresent()) {
            model.addAttribute("quiz", quizOpt.get());
            model.addAttribute("question", questionOpt.get());
            model.addAttribute("difficultyLevels", Question.DifficultyLevel.values());
            
            // Find the index of the correct answer option to pre-select it in the form
            List<AnswerOption> answerOptions = questionOpt.get().getAnswerOptions();
            for (int i = 0; i < answerOptions.size(); i++) {
                if (answerOptions.get(i).isCorrect()) {
                    model.addAttribute("correctChoiceIndex", i);
                    break;
                }
            }

            return "question-form";
        } else {
            model.addAttribute("errorMessage", 
                    questionOpt.isPresent() ? "Quiz not found!" : "Question not found!");
            return "redirect:/quizzes/" + quizId + "/questions";
        }
    }

    @PostMapping("/{quizId}/questions/{questionId}/update")
    public String updateQuestion(
            @PathVariable Long quizId,
            @PathVariable Long questionId,
            @ModelAttribute Question updatedQuestion,
            @RequestParam("correctChoiceIndex") Integer correctChoiceIndex,
            RedirectAttributes redirectAttributes) {
        
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isPresent()) {
            Question question = questionOpt.get();
            
            // Update basic fields
            question.setQuestionText(updatedQuestion.getQuestionText());
            question.setDifficultyLevel(updatedQuestion.getDifficultyLevel());
            question.setPoints(updatedQuestion.getPoints());
            
            // Update answer options
            List<AnswerOption> existingOptions = question.getAnswerOptions();
            List<AnswerOption> updatedOptions = updatedQuestion.getAnswerOptions();
            
            for (int i = 0; i < Math.min(existingOptions.size(), updatedOptions.size()); i++) {
                AnswerOption existingOption = existingOptions.get(i);
                AnswerOption updatedOption = updatedOptions.get(i);
                
                existingOption.setAnswerText(updatedOption.getAnswerText());
                existingOption.setCorrect(i == correctChoiceIndex);
            }
            
            questionRepository.save(question);
            redirectAttributes.addFlashAttribute("message", "Question updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Question not found!");
        }
        
        return "redirect:/quizzes/" + quizId + "/questions";
    }

    @PostMapping("/{quizId}/questions/{questionId}/delete")
    public String deleteQuestion(
            @PathVariable Long quizId,
            @PathVariable Long questionId,
            RedirectAttributes redirectAttributes) {
        
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isPresent()) {
            questionRepository.delete(questionOpt.get());
            redirectAttributes.addFlashAttribute("message", "Question deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Question not found!");
        }
        
        return "redirect:/quizzes/" + quizId + "/questions";
    }

    // Teacher Management
    @GetMapping("/teachers")
    public String getAllTeachers(Model model) {
        List<Teacher> teachers = teacherRepository.findAll();
        model.addAttribute("teachers", teachers);
        return "teacher-list";
    }

    @GetMapping("/teachers/new")
    public String showCreateTeacherForm(Model model) {
        model.addAttribute("teacher", new Teacher());
        return "teacher-form";
    }

    @PostMapping("/teachers/save")
    public String createTeacher(
            @ModelAttribute Teacher teacher,
            RedirectAttributes redirectAttributes) {
        teacherRepository.save(teacher);
        redirectAttributes.addFlashAttribute("message", "Teacher created successfully!");
        return "redirect:/quizzes/teachers";
    }
}