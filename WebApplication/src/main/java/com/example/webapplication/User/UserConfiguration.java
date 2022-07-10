package com.example.webapplication.User;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfiguration {
    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository){
        return args->{
           User firstUser= new User(
                    "alex","alex2001","Alexandros","Tsalapatis",
                    "alex@gmail.com",679120494,"Athens,32","123","user",
                    "Greece");

          userRepository.saveAll(List.of(firstUser));

        };
    }
}
