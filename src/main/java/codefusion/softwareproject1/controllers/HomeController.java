package codefusion.softwareproject1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    // This serves as a simple health check endpoint
    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "Service is up and running!";
    }
}
