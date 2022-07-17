package com.example.webapplication.ApplicationControllers;

import com.example.webapplication.User.User;
import com.example.webapplication.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/users/login")
    public ResponseEntity<?> login(@RequestBody User user) throws Exception {
        return userService.login(user.getEmail(), user.getPassword());
    }

}
