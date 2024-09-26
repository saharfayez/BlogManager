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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest
class PostControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    final String userName = "sahar";
    final String password = "0000";
    final String token = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYWhhciIsImlhdCI6MTcyNzI2Mzc1NSwiZXhwIjoxNzU4Nzk5NzU1fQ.5_Lowj5eWW51Yaa3APWfp9a23_CYg1uViAscY7oC8Ro";
    final String testToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWwiLCJpYXQiOjE3MjczMzk1MjMsImV4cCI6MTc1ODg3NTUyM30.3WUn_5vN-L9N1H3Ov_4LHV2F1CCaNOv2hi90nNysGy0";

    final String header = "Authorization";
    Long id;

    @BeforeEach
    void setUp() {

        User user = User.builder()
                .userName(userName)
                .password(password)
                .build();

        User user2 = User.builder()
                .userName("aml")
                .password("0000")
                .build();

        userRepository.save(user);
        userRepository.save(user2);


        Post post = Post.builder()
                .title("test title")
                .content("test content")
                .user(user)
                .build();
        postRepository.save(post);

        id = post.getId();
    }

    @Test
    @WithMockUser
    @Transactional
    public void testHello() throws Exception {

        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    @Transactional
    void addPostTest() throws Exception {
        PostDto postDto = PostDto.builder()
                .title("test")
                .content("i am testing now")
                .build();

        String response = mockMvc.perform(post("/posts")
                        .content(objectMapper.writeValueAsString(postDto))
                        .header(header, token)
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

    @Test
    @WithMockUser
    @Transactional
    public void getAllPostsTest() throws Exception {

        String response = mockMvc.perform(get("/posts")
                        .header(header, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<PostView> postViewList = List.of(objectMapper.readValue(response, PostView[].class));

        List<Post> posts = postRepository.findAll();

        assertThat(posts).usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(postViewList);

    }

    @Test
    @WithMockUser
    @Transactional
    public void getPostByIdTest() throws Exception {

        String response = mockMvc.perform(get("/posts/" + id)
                        .header(header, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.content").value("test content"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PostView postView = objectMapper.readValue(response, PostView.class);

        Post post = postRepository.findById(id).get();

        assertThat(post).usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(postView);
    }

    @Test
    @WithMockUser
    @Transactional
    public void unauthorizedUpdatePostTest() throws Exception {

        PostDto postDto = PostDto.builder()
                .title("test")
                .content("i am testing now")
                .build();

        mockMvc.perform(put("/posts/" + id)
                        .content(objectMapper.writeValueAsString(postDto))
                        .header(header, testToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser
    @Transactional
    public void updatePostTest() throws Exception {
        System.out.println(id);
        PostDto postDto = PostDto.builder()
                .title("test")
                .content("i am testing now")
                .build();

        String response = mockMvc.perform(put("/posts/" + id)
                        .content(objectMapper.writeValueAsString(postDto))
                        .header(header, token)
                        .contentType(MediaType.APPLICATION_JSON)
                )

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PostView postView = objectMapper.readValue(response, PostView.class);

        Post post = postRepository.findById(id).get();

        assertThat(postView).usingRecursiveComparison()
                .ignoringActualNullFields()
                .ignoringExpectedNullFields()
                .isEqualTo(postDto);

        assertThat(post).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringActualNullFields()
                .isEqualTo(postView);

    }

    @Test
    @WithMockUser
    @Transactional
    public void deletePostTest() throws Exception {

        mockMvc.perform(delete("/posts/" + id)
                        .header(header, token))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Post> posts = postRepository.findAll();

        assertThat(posts.size()).isEqualTo(0);
    }

    @Test
    @WithMockUser
    @Transactional
    public void unauthorizedDeletePostTest() throws Exception {

        mockMvc.perform(delete("/posts/" + id)
                        .header(header, testToken))
                .andExpect(status().isForbidden());

    }

}