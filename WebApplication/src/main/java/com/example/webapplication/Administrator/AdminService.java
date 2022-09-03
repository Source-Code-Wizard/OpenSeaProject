package com.example.webapplication.Administrator;

import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRepository;
import com.example.webapplication.User.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service // (1)
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository,UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository=userRepository;
    }

    public List<UserRequestDto> getRegistrationRequests(){
        List<UserRequestDto> userRequestDtoList = new ArrayList<>();
        List<User> userList= adminRepository.showRegistrationRequests();
        for (int i = 0; i < userList.size(); i++) {
            User UnregisteredUser = userList.get(i);
            userRequestDtoList.add(new UserRequestDto(UnregisteredUser.getUsername(),UnregisteredUser.getEmail()));
        }
        return userRequestDtoList;
    }
    public ResponseEntity<?> authenticateUser(String userName){
        Optional<User> user= userRepository.findByUsername(userName);
        if (user.isPresent()){
            User userForAuthentication = user.get();
            userForAuthentication.setRegistered(true);
            userRepository.save(userForAuthentication);
            return new ResponseEntity<>("User "+userForAuthentication.getUsername()+" is now authenticated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("This user can not be authenticated", HttpStatus.BAD_REQUEST);
    }
}
