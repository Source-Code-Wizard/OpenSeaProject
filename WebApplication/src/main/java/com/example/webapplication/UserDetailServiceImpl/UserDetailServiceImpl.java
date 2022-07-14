/*
package com.example.webapplication.UserDetailServiceImpl;

import com.example.webapplication.User.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static java.util.Collections.emptyList;

public class UserDetailServiceImpl implements UserDetailsService {

    private UserRepository applicationUserRepository;

    public void UserDetailsServiceImpl(UserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User(
                applicationUser.getEmail(), applicationUser.getPassword(), new ArrayList<>());
        //return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }
}
*/
