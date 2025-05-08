package codefusion.softwareproject1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home(Model model) {
        
        return "home";
    }
    
    
    @GetMapping("/api-info")
    @ResponseBody
    public String apiInfo() {
        return "Welcome to the Quiz Application API!<br><br>" +
               "Available endpoints:<br>" +
               "- <a href='/api/quizzes'>/api/quizzes</a> - List all quizzes<br>" +
               "- <a href='/api/quizzes/teachers'>/api/quizzes/teachers</a> - List all teachers<br>";
    }
    
    
    @GetMapping("/dashboard")
    public String dashboard() {
       
        return "dashboard";
    }
    
   
    @GetMapping("/dashboard/teacher")
    public String teacherDashboard() {
       
        return "teacher-dashboard";
    }
    
    
    @GetMapping("/dashboard/student")
    public String studentDashboard() {
    
        return "student-dashboard";
    }
    
    
    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "Service is up and running!";
    }
}