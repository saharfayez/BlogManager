package com.example.BloggerApplication.controllers;

import com.example.BloggerApplication.dto.UserDto;
import com.example.BloggerApplication.response.LoginResponse;
import com.example.BloggerApplication.response.SignupResponse;
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

    @PostMapping("/auth/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody UserDto userDto) {

            SignupResponse signupResponse = userService.signup(userDto);

            return new ResponseEntity<>(signupResponse , HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody UserDto userDto) {

        LoginResponse loginResponse = userService.login(userDto);

        return new ResponseEntity<>(loginResponse , HttpStatus.OK);

    }


    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
