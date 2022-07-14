package com.example.webapplication.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

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

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    /* Save a specific User to the data base! */
    public User registerUserToBase(User userForRegistration){
        return userRepository.save(userForRegistration);
    }



    public Optional<User>  getUserByUserName(String userName){
        return userRepository.findByUsername(userName);
    }

  /*  @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword());
    }

*/
}
