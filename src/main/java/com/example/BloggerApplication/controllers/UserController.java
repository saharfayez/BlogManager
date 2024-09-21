package com.example.BloggerApplication.controllers;

import com.example.BloggerApplication.dto.UserDto;
import com.example.BloggerApplication.entites.User;
import com.example.BloggerApplication.response.LoginResponse;
import com.example.BloggerApplication.services.JwtService;
import com.example.BloggerApplication.services.UserDetailsServiceImpl;
import com.example.BloggerApplication.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth/signup")
    public ResponseEntity<User> register(@Valid @RequestBody UserDto userDto) {
            User registeredUser = userService.signup(userDto);
            return ResponseEntity.ok(registeredUser);

    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody UserDto userDto) {
        User authenticatedUser = userService.login(userDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}
