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
public class SignUpController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/users/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) throws Exception {
       // return userService.signUp(user);
        return new ResponseEntity<>(userService.signUp(user), HttpStatus.CREATED);
    }
}
