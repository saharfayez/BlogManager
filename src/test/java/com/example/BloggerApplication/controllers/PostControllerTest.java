package com.example.BloggerApplication.controllers;

import com.example.BloggerApplication.dto.PostDto;
import com.example.BloggerApplication.entites.Post;
import com.example.BloggerApplication.entites.User;
import com.example.BloggerApplication.repositories.PostRepository;
import com.example.BloggerApplication.repositories.UserRepository;
import com.example.BloggerApplication.views.PostView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    final String userName = "sahar";

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .userName(userName)
                .password("0000")
                .build();
        userRepository.save(user);
    }

    @Test
    @Transactional
    void addPost() throws Exception {
        PostDto postDto = PostDto.builder()
                .title("test")
                .content("i am testing now")
                .userName(userName)
                .build();

        String response = mockMvc.perform(post("/posts")
                        .content(objectMapper.writeValueAsString(postDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PostView returnedPost = objectMapper.readValue(response, PostView.class);

        Post post = postRepository.findById(returnedPost.getId()).get();

        assertThat(returnedPost).usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(postDto);

        assertThat(post).usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(postDto);

    }
}
