package com.example.webapplication.ApplicationControllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomePageController {
    @GetMapping("/dashboard")
    public String dashboard() {
        return "Displaying the dashboard page contents";
    }
    @GetMapping("/home")
    public String home() {
        return "Displaying the home page contents";
    }
}


