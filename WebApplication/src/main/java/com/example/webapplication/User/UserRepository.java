package com.example.webapplication.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/* this interface implements the dataBase access-layer of the application */
@Repository
public interface UserRepository extends JpaRepository<User,UserCompositePK> { }
