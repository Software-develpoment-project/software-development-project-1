package codefusion.softwareproject1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "Welcome to the Quiz Application API!<br><br>" +
               "Available endpoints:<br>" +
               "- <a href='/api/quizzes'>/api/quizzes</a> - List all quizzes<br>" +
               "- <a href='/api/quizzes/teachers'>/api/quizzes/teachers</a> - List all teachers<br>";
    }
    
    // This serves as a simple health check endpoint
    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "Service is up and running!";
    }
} 