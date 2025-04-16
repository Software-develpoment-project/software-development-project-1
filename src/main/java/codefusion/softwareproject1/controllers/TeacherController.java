package codefusion.softwareproject1.controllers;


import codefusion.softwareproject1.DTO.QuizDTO;
import codefusion.softwareproject1.Models.QuizClass;
import codefusion.softwareproject1.exceptions.ResourceNotFoundException;
import codefusion.softwareproject1.repo.QuizRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private QuizRepo quizRepository;

    @GetMapping("/{id}")
    public String getQuizes(@PathVariable Long id, @ResponseBody) {
        // Fetch the teacher by ID
        TeacherClass teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        // Fetch quizzes associated with the teacher
        List<QuizClass> quizzes = quizRepository.findByTeachersId(id);

        // Return the quizzes as a response
        return "quizes" ;
    }


    @PostMapping("/add/quiz")
    public String createQuiz(@RequestBody QuizClass quizDTO) {
        // Validate the input
        if (quizDTO.getName() == null || quizDTO.getDescription() == null) {
            return "Quiz name and description are required.";
        }

        // Map QuizDTO to QuizClass
        QuizClass quiz = new QuizClass();
        quiz.setName(quizDTO.getName());
        quiz.setDescription(quizDTO.getDescription());
        quiz.setPublished(quizDTO.isPublished());

        // Save the quiz to the database
        quizRepository.save(quiz);

        return "redirect:/quizes";
    }

    @PostMapping("/edit/{id}")
    public String editQuiz(@PathVariable Long id, Model model) throws IllegalAccessException {
        QuizClass quiz = quizRepository.findById(id)
                .orElseThrow(() -> new IllegalAccessException("Quiz not found with id: " + id));

        model.addAttribute("quiz", quiz) ;
        

        quizRepository.save(quiz);
        return "redirect:/quizes";
    }
}