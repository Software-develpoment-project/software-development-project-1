package codefusion.softwareproject1.controllers;

import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.Models.ChoiceClass;
import codefusion.softwareproject1.Models.QuestionsClass;
import codefusion.softwareproject1.Models.TeacherClass;
import codefusion.softwareproject1.repo.QuizRepo;
import codefusion.softwareproject1.repo.QuestionRepo;
import codefusion.softwareproject1.repo.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quizzes")
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
        List<QuizClass> quizzes = quizRepository.findAll();
        model.addAttribute("quizzes", quizzes);
        return "quiz-list";
    }

    @GetMapping("/new")
    public String showCreateQuizForm(Model model) {
        QuizClass quiz = new QuizClass();
        List<TeacherClass> teachers = teacherRepository.findAll();

        model.addAttribute("quiz", quiz);
        model.addAttribute("teachers", teachers);
        model.addAttribute("difficulties", QuizClass.Difficulty.values());
        model.addAttribute("topics", QuizClass.Topic.values());
        return "quiz-form";
    }

    @PostMapping("/save")
    public String createQuiz(@ModelAttribute QuizClass quiz, RedirectAttributes redirectAttributes) {
        if (quiz.getTeacher() != null && quiz.getTeacher().getId() != null) {
            TeacherClass teacher = teacherRepository.findById(quiz.getTeacher().getId()).orElse(null);
            quiz.setTeacher(teacher);
        }

        quizRepository.save(quiz);
        redirectAttributes.addFlashAttribute("message", "Quiz created successfully!");
        return "redirect:/quizzes";
    }

    @GetMapping("/{id}")
    public String getQuizById(@PathVariable Long id, Model model) {
        Optional<QuizClass> quizOpt = quizRepository.findById(id);
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
        Optional<QuizClass> quizOpt = quizRepository.findById(id);
        if (quizOpt.isPresent()) {
            List<TeacherClass> teachers = teacherRepository.findAll();
            model.addAttribute("quiz", quizOpt.get());
            model.addAttribute("teachers", teachers);
            model.addAttribute("difficulties", QuizClass.Difficulty.values());
            model.addAttribute("topics", QuizClass.Topic.values());
            return "quiz-form";
        } else {
            model.addAttribute("errorMessage", "Quiz not found!");
            return "redirect:/quizzes";
        }
    }

    @PostMapping("/update")
    public String updateQuiz(@ModelAttribute QuizClass updatedQuiz, RedirectAttributes redirectAttributes) {
        Optional<QuizClass> quizOpt = quizRepository.findById(updatedQuiz.getId());
        if (quizOpt.isPresent()) {
            QuizClass quiz = quizOpt.get();
            quiz.setName(updatedQuiz.getName());
            quiz.setDescription(updatedQuiz.getDescription());
            quiz.setDifficulty(updatedQuiz.getDifficulty());
            quiz.setTopic(updatedQuiz.getTopic());
            quiz.setPublished(updatedQuiz.isPublished());

            if (updatedQuiz.getTeacher() != null && updatedQuiz.getTeacher().getId() != null) {
                TeacherClass teacher = teacherRepository.findById(updatedQuiz.getTeacher().getId()).orElse(null);
                quiz.setTeacher(teacher);
            } else {
                quiz.setTeacher(null);
            }

            quizRepository.save(quiz);
            redirectAttributes.addFlashAttribute("message", "Quiz updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Quiz not found!");
        }
        return "redirect:/quizzes";
    }

    @PostMapping("/{id}/delete")
    public String deleteQuiz(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<QuizClass> quizOpt = quizRepository.findById(id);
        if (quizOpt.isPresent()) {
            quizRepository.delete(quizOpt.get());
            redirectAttributes.addFlashAttribute("message", "Quiz deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Quiz not found!");
        }
        return "redirect:/quizzes";
    }

    // Question Management
    // Make sure to add this import

    // Question Management
    @GetMapping("/{quizId}/questions")
    public String getQuestionsByQuiz(@PathVariable Long quizId, Model model) {
        Optional<QuizClass> quizOpt = quizRepository.findById(quizId);
        if (quizOpt.isPresent()) {
            QuizClass quiz = quizOpt.get();
            List<QuestionsClass> questions = questionRepository.findByQuizId(quizId);

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
        Optional<QuizClass> quizOpt = quizRepository.findById(quizId);
        if (quizOpt.isPresent()) {
            QuizClass quiz = quizOpt.get();
            QuestionsClass question = new QuestionsClass();

            // Initialize the question with the quiz
            question.setQuiz(quiz);

            // Create default empty choices
            List<ChoiceClass> choices = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                ChoiceClass choice = new ChoiceClass();
                choice.setQuestion(question);
                choices.add(choice);
            }
            question.setChoices(choices);

            model.addAttribute("quiz", quiz);
            model.addAttribute("question", question);
            model.addAttribute("difficultyLevels", QuestionsClass.DifficultyLevel.values());
            return "question-form";
        } else {
            model.addAttribute("errorMessage", "Quiz not found!");
            return "redirect:/quizzes";
        }
    }

    @PostMapping("/{quizId}/questions/save")
    public String createQuestion(
            @PathVariable Long quizId,
            @ModelAttribute QuestionsClass question,
            @RequestParam("correctChoiceIndex") Integer correctChoiceIndex,
            RedirectAttributes redirectAttributes) {

        Optional<QuizClass> quizOpt = quizRepository.findById(quizId);
        if (quizOpt.isPresent()) {
            QuizClass quiz = quizOpt.get();
            question.setQuiz(quiz);

            // Set correct answer
            if (correctChoiceIndex != null && correctChoiceIndex >= 0 &&
                    correctChoiceIndex < question.getChoices().size()) {
                for (int i = 0; i < question.getChoices().size(); i++) {
                    ChoiceClass choice = question.getChoices().get(i);
                    choice.setCorrect(i == correctChoiceIndex);
                    choice.setQuestion(question);
                }
            }

            questionRepository.save(question);
            redirectAttributes.addFlashAttribute("message", "Question added successfully!");
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

        Optional<QuizClass> quizOpt = quizRepository.findById(quizId);
        Optional<QuestionsClass> questionOpt = questionRepository.findById(questionId);

        if (quizOpt.isPresent() && questionOpt.isPresent()) {
            QuizClass quiz = quizOpt.get();
            QuestionsClass question = questionOpt.get();

            model.addAttribute("quiz", quiz);
            model.addAttribute("question", question);
            model.addAttribute("difficultyLevels", QuestionsClass.DifficultyLevel.values());
            return "question-form";
        } else {
            model.addAttribute("errorMessage", "Quiz or Question not found!");
            return "redirect:/quizzes";
        }
    }

    @PostMapping("/{quizId}/questions/{questionId}/update")
    public String updateQuestion(
            @PathVariable Long quizId,
            @PathVariable Long questionId,
            @ModelAttribute QuestionsClass updatedQuestion,
            @RequestParam("correctChoiceIndex") Integer correctChoiceIndex,
            RedirectAttributes redirectAttributes) {

        Optional<QuestionsClass> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isPresent()) {
            QuestionsClass question = questionOpt.get();
            question.setContent(updatedQuestion.getContent());
            question.setDifficultyLevel(updatedQuestion.getDifficultyLevel());

            // Update choices and set correct answer
            List<ChoiceClass> existingChoices = question.getChoices();
            List<ChoiceClass> updatedChoices = updatedQuestion.getChoices();

            // Update existing choices or create new ones
            for (int i = 0; i < updatedChoices.size(); i++) {
                ChoiceClass updatedChoice = updatedChoices.get(i);

                if (i < existingChoices.size()) {
                    // Update existing choice
                    ChoiceClass existingChoice = existingChoices.get(i);
                    existingChoice.setText(updatedChoice.getText());
                    existingChoice.setCorrect(i == correctChoiceIndex);
                } else {
                    // Add new choice
                    ChoiceClass newChoice = new ChoiceClass();
                    newChoice.setText(updatedChoice.getText());
                    newChoice.setCorrect(i == correctChoiceIndex);
                    newChoice.setQuestion(question);
                    existingChoices.add(newChoice);
                }
            }

            // Remove extra choices if needed
            while (existingChoices.size() > updatedChoices.size()) {
                existingChoices.remove(existingChoices.size() - 1);
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

        Optional<QuestionsClass> questionOpt = questionRepository.findById(questionId);
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
        List<TeacherClass> teachers = teacherRepository.findAll();
        model.addAttribute("teachers", teachers);
        return "teachers-list";
    }

    @GetMapping("/teachers/new")
    public String showCreateTeacherForm(Model model) {
        TeacherClass teacher = new TeacherClass();
        model.addAttribute("teacher", teacher);
        return "teacher-form";
    }

    @PostMapping("/teachers/save")
    public String createTeacher(
            @ModelAttribute TeacherClass teacher,
            RedirectAttributes redirectAttributes) {

        teacherRepository.save(teacher);
        redirectAttributes.addFlashAttribute("message", "Teacher added successfully!");
        return "redirect:/quizzes/teachers";
    }

}