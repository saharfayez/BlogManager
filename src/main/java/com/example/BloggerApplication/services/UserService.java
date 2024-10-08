package com.example.BloggerApplication.services;

import com.example.BloggerApplication.dto.UserDto;
import com.example.BloggerApplication.entites.User;
import com.example.BloggerApplication.exception.ObjectNotFoundException;
import com.example.BloggerApplication.exception.UserAlreadyExistsException;
import com.example.BloggerApplication.repositories.UserRepository;
import com.example.BloggerApplication.response.LoginResponse;
import com.example.BloggerApplication.response.SignupResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ModelMapper modelMapper;

    public User findUser(String userName) {

        return userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new ObjectNotFoundException("User not found : " + userName)
                );
    }

    public SignupResponse signup(UserDto userDto) {

        if (userRepository.existsByUserName(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists. Please choose another one.");
        }

        User user = new User();
        user.setUserName(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);

        SignupResponse signupResponse = SignupResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();

        return signupResponse;

    }

    public LoginResponse login(UserDto userDto) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

        String jwtToken = jwtService.generateToken(modelMapper.map(userDto, User.class));

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return loginResponse;
    }

}
