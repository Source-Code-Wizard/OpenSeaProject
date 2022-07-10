
/* this class will implement the logic of the API! */

package com.example.webapplication.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/* this is the api implementation that contacts the service layer*/
@RestController
@RequestMapping(path="api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired// This implements the dependency injection design pattern
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User>getUser(){
        return userService.getUser();
    }
}
