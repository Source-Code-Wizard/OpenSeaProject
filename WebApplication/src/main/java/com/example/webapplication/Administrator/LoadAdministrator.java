package com.example.webapplication.Administrator;

import com.example.webapplication.Role.Role;
import com.example.webapplication.Role.RoleRepository;
import com.example.webapplication.Seller.Seller;
import com.example.webapplication.Seller.SellerRepository;
import com.example.webapplication.User.User;
import com.example.webapplication.User.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoadAdministrator {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SellerRepository sellerRepository;

    @Bean("LoadAdministrator")
    @DependsOn({"LoadRoles"})
    public CommandLineRunner StoreAdministrator(UserRepository userRepository,RoleRepository roleRepository,SellerRepository sellerRepository){

        if(userRepository.findByUsername("admin").isEmpty()){
            User Administrator = new User("admin","$2a$12$wyob3D/zHrRlLgRJwkycq.mvQwu732mdJjBZbg.c0O93CFiN.SpiC", "admin@gmail.com");
            Role adminRole = roleRepository.findByName("ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            Administrator.setRoles(roles);


            Seller seller = new Seller(0,1L);
            seller.setUser(Administrator);
            seller.setUsername(Administrator.getUsername());
            Administrator.setSeller(seller);

            userRepository.save(Administrator);
        }

       return  args -> {
         log.info("Administrator was registered to Database!");
       };
    }
}
