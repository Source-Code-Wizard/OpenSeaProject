package com.example.webapplication.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* (1): Since we want to implement the D.I. pattern , we have to instantiate ( with anotation: @Service) the UserService class since we pass
* a refrence of this class to UserController constructor!
*/

/* The service layer uses the repository interface to retrive data from tha database!*/
@Service // (1)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUser(){
        return userRepository.findAll();
    }
}