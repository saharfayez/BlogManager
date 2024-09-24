package com.example.BloggerApplication.controllers;

import com.example.BloggerApplication.dto.PostDto;
import com.example.BloggerApplication.dto.UserDto;
import com.example.BloggerApplication.entites.Post;
import com.example.BloggerApplication.entites.User;
import com.example.BloggerApplication.repositories.PostRepository;
import com.example.BloggerApplication.repositories.UserRepository;
import com.example.BloggerApplication.services.PostService;
import com.example.BloggerApplication.views.PostView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration
class PostControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    final String userName = "sahar";
    final String password = "0000";

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .userName(userName)
                .password(password)
                .build();
        userRepository.save(user);
    }

//    @Test
//    void getPosts() throws Exception {
//        mockMvc.perform(get("/posts"))
//                .andExpect(status().isOk());
//
//    }
//    @Test
//    void testSignup() throws Exception {
//
//        UserDto userDto = new UserDto("testUser", "testPassword");
//
//        mockMvc.perform(post("/auth/signup")
//
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(userDto))
//        )
//                .andExpect(status().isCreated());
//
//    }

//    @Test
//    public void shouldAuthenticateUser() throws Exception {
//        UserDto userDto = new UserDto("testUser", "testPassword");
//
//        mockMvc.perform(post("/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(userDto)))
//                .andExpect(status().isOk());
//    }


    @Test
    @WithMockUser
    public void testHello() throws Exception {

        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void addPost() throws Exception {
        PostDto postDto = PostDto.builder()
                .title("test")
                .content("i am testing now")
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





















//
//    @Test
//    @Transactional
//    void getAllPosts() throws Exception {
//
//        mockMvc.perform(get("/posts"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].title").value("test"))
//                .andExpect(jsonPath("$[0].content").value("content"))
//                .andExpect(jsonPath("$.size()").value(1));
//    }
//
//    @Test
//    @Transactional
//    void getPostById() throws Exception {
//
//        PostView postView = postService.getPostById(2L);
//        mockMvc.perform(get("/posts/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value(postView.getTitle()))
//                .andExpect(jsonPath("$.content").value(postView.getContent()))
//                .andExpect(jsonPath("$.id").value(postView.getId()));
//
//    }
}
