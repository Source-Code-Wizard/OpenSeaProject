package com.example.webapplication.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoadRoles {


    private final RoleRepository roleRepository;

    @Bean("LoadRoles")
    public CommandLineRunner StoreRoles(){

        roleRepository.save(new Role(1,"ADMIN"));
        roleRepository.save(new Role(2,"USER"));
        roleRepository.save(new Role(3,"BIDDER"));
        roleRepository.save(new Role(4,"SELLER"));

        return args -> {
            log.info("Store User-Roles to Database!");
        };
    }
}
