package com.example.BloggerApplication.repositories;

import com.example.BloggerApplication.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserName(String userName);
    Boolean existsByUserName(String userName);
}
