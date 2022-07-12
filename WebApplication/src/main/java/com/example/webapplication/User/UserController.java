
/* this class will implement the logic of the API! */

package com.example.webapplication.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/* this is the api implementation that contacts the service layer*/

/*  This code uses Spring @RestController annotation,
    which marks the class as a controller where every method returns a domain object (JSON) instead of a view.
*/
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired// This implements the dependency injection design pattern
    public UserController(UserService userService) {
        this.userService = userService;
    }
/*

    @GetMapping("/users")
    public List<User>getUser(){
        return userService.getUser();
    }

    @GetMapping("/alex")
    public User helloUser() {
        return userService.getSpecificUser();
    }
*/


    /*
        Simply put, the @RequestBody annotation maps the HttpRequest body to a transfer or domain object,
        enabling automatic deserialization of the inbound HttpRequest body onto a Java object.(domain object to java object)
    */
    @PostMapping()
    public ResponseEntity<User>registerUserToBase(@RequestBody User userForRegistration){
        return new ResponseEntity<>(userService.registerUserToBase(userForRegistration), HttpStatus.CREATED);
    }
}
