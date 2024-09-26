package com.example.BloggerApplication.controllers;

import com.example.BloggerApplication.dto.UserDto;
import com.example.BloggerApplication.entites.User;
import com.example.BloggerApplication.repositories.UserRepository;
import com.example.BloggerApplication.response.SignupResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder ;


    final String userName = "sahar";
    final String password = "0000";

    @BeforeEach
    void setUp() {

        User user = User.builder()
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);
    }


    @Test
    @Transactional
    void signupTest() throws Exception {

        String userName = "salma";
        String password = "0000";

        UserDto userDto = UserDto.builder()
                .username(userName)
                .password(password)
                .build();

        String response = mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        SignupResponse signupResponse = objectMapper.readValue(response, SignupResponse.class);

        User user = userRepository.findUserByUserName(userName).get();

        assertThat(signupResponse).usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(userDto);

        assertThat(user).usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(signupResponse);

    }

    @Test
    @Transactional
    void userAlreadyExistsTest() throws Exception {

        UserDto userDto = UserDto.builder()
                .username(userName)
                .password(password)
                .build();

        mockMvc.perform(post("/auth/signup")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    void loginTest() throws Exception {

        UserDto userDto = UserDto.builder()
                .username(userName)
                .password(password )
                .build();

        mockMvc.perform(post("/auth/login")
                .content(objectMapper.writeValueAsString(userDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @Transactional
    void userUnauthorizedTest() throws Exception {

        UserDto userDto = UserDto.builder()
                .username("salma")
                .password("0000")
                .build();

        mockMvc.perform(post("/auth/login")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }
}