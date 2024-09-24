package com.example.BloggerApplication.services;

import com.example.BloggerApplication.dto.PostDto;
import com.example.BloggerApplication.entites.Post;
import com.example.BloggerApplication.entites.User;
import com.example.BloggerApplication.exception.ObjectNotFoundException;
import com.example.BloggerApplication.repositories.PostRepository;
import com.example.BloggerApplication.views.PostView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtService jwtService;

    public PostView addPost(HttpServletRequest request, PostDto postDto) {

        String username = jwtService.getUsernameFromToken(request);

        Post post = modelMapper.map(postDto, Post.class);

        User user = userService.findUser(username);

        post.setUser(user);

        Post savedPost = postRepository.save(post);

        return modelMapper.map(savedPost, PostView.class);
    }

    public List<PostView> getAllPosts() {

        List<Post> posts = postRepository.findAll();

        List<PostView> postViews = List.of(modelMapper.map(posts, PostView[].class));

        return postViews;
    }

    public PostView getPostById(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Post not found : " + id)
                );

        return modelMapper.map(post, PostView.class);
    }

    public PostView updatePost(HttpServletRequest request, Long id, PostDto postDto) {

        if (!authorizePost(request, id)) {
            throw new AccessDeniedException("User not allowed to update or this post.");

        }

        String username = jwtService.getUsernameFromToken(request);
        Post post = modelMapper.map(postDto, Post.class);
        post.setId(id);
        post.setUser(userService.findUser(username));
        Post savedPost = postRepository.save(post);
        return modelMapper.map(savedPost, PostView.class);
    }

    public PostView deletePost(HttpServletRequest request, Long id) {

        if (!authorizePost(request, id)) {
            throw new AccessDeniedException("User not allowed to delete this post.");
        }

        PostView postView = getPostById(id);
        postRepository.deleteById(id);
        return postView;
    }


    public boolean authorizePost(HttpServletRequest request, Long id) {
        String username = jwtService.getUsernameFromToken(request);
        PostView postView = getPostById(id);
        return postView.getUserName().equals(username);
    }
}
