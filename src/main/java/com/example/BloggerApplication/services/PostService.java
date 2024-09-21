package com.example.BloggerApplication.services;

import com.example.BloggerApplication.dto.PostDto;
import com.example.BloggerApplication.entites.Post;
import com.example.BloggerApplication.entites.User;
import com.example.BloggerApplication.repositories.PostRepository;
import com.example.BloggerApplication.views.PostView;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    public PostView addPost(PostDto postDto) {
        Post post = modelMapper.map(postDto, Post.class);
        User user = userService.findUser(postDto.getUserName());
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
        Optional<Post> post = postRepository.findById(id);
        PostView postView = modelMapper.map(post, PostView.class);
        return postView;
    }
}
