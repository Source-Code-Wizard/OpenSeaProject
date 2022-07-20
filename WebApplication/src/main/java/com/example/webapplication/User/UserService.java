package com.example.webapplication.User;

import com.example.webapplication.Administrator.AdminRepository;
import com.example.webapplication.Role.Role;
import com.example.webapplication.Role.RoleRepository;
import com.example.webapplication.WebConfiguration.AuthenticatedUser;
import com.example.webapplication.WebConfiguration.JWTs.JWTutils;
import com.example.webapplication.WebConfiguration.JWTs.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/*
* (1): Since we want to implement the D.I. pattern , we have to instantiate ( with anotation: @Service) the UserService class since we pass
* a refrence of this class to UserController constructor!
*/

/* The service layer uses the repository interface to retrive data from tha database!*/
@Service // (1)
public class UserService {

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;

    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager,
                       BCryptPasswordEncoder passwordEncoder , RoleRepository roleRepository,AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.authenticationManager=authenticationManager;
        this.passwordEncoder=passwordEncoder;
        this.roleRepository=roleRepository;
        this.adminRepository=adminRepository;
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

    public ResponseEntity<?> login(String userName,String userPassword) {
        try {
            Optional<User> user= userRepository.findByUsername(userName);
          if(!user.get().isRegistered())
                return new ResponseEntity<>("Administrator has to accept this user first...", HttpStatus.BAD_REQUEST);

            //String userEmail=user.get().getEmail();
            Authentication authObject= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));

            SecurityContextHolder.getContext().setAuthentication(authObject);

            String jwt = JWTutils.generateJwtToken(authObject);

            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authObject.getPrincipal();

            List<String> roles = authenticatedUser.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt, authenticatedUser.getUsername(), authenticatedUser.getEmail(), roles));

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid credentials!", HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> signUp(@RequestBody User user){

        // add check for username exists in a DB
        if(userRepository.existsByUsername(user.getUsername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        // add check for email exists in DB
        if(userRepository.existsByEmail(user.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        //first we need to convert the password to a bcrypt password type!
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        /* Assign user permission to new user!*/
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("USER");
        roles.add(userRole);
        user.setRoles(roles);

        // register User to database!
        userRepository.save(user);

        return new ResponseEntity<>("This registration request needs to be authenticated first by the administrator!", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteAllUsers(){
        userRepository.deleteAll();
        return new ResponseEntity<>("All users have been deleted!", HttpStatus.OK);
    }


}
