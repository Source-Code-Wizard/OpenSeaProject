package com.example.webapplication.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/* this interface implements the dataBase access-layer of the application */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
