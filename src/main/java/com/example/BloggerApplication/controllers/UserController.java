package com.example.BloggerApplication.controllers;

import com.example.BloggerApplication.dto.UserDto;
import com.example.BloggerApplication.entites.User;
import com.example.BloggerApplication.response.LoginResponse;
import com.example.BloggerApplication.services.JwtService;
import com.example.BloggerApplication.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/auth/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody UserDto userDto) {

            User registeredUser = userService.signup(userDto);

            return new ResponseEntity<>(registeredUser , HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserDto userDto) {

        String jwtToken = userService.login(userDto);

        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setToken(jwtToken);

        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return new ResponseEntity<>(loginResponse , HttpStatus.OK);

    }
}
