package com.example.webapplication.Administrator;

import com.example.webapplication.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdministratorController {
    private final AdminService adminService;

    @Autowired// This implements the dependency injection design pattern
    public AdministratorController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getRegRequests")
    public List<User> getRegistrationRequests(){
        return adminService.getRegistrationRequests();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/getRegRequests/{userName}")
    public ResponseEntity<?> authenticateUser(@PathVariable("userName") String userName){
        return new ResponseEntity<>(adminService.authenticateUser(userName), HttpStatus.OK);
    }
}
