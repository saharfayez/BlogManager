package com.example.BloggerApplication.services;

import com.example.BloggerApplication.entites.User;
import com.example.BloggerApplication.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {

        Optional<User> user = userRepository.findUserByUserName(username);

        return user
                .orElseThrow(() -> new UsernameNotFoundException("user not found" + username));
    }
}
