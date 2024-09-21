package com.example.BloggerApplication.controllers;

import com.example.BloggerApplication.dto.PostDto;
import com.example.BloggerApplication.services.PostService;
import com.example.BloggerApplication.views.PostView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostView> addPost(@Valid @RequestBody PostDto postDto) {

        PostView addedPost = postService.addPost(postDto);

        return new ResponseEntity<>(addedPost, HttpStatus.CREATED);
    }
    @GetMapping("/posts")
    public ResponseEntity<List<PostView>> getAllPosts() {

        List<PostView> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);

    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostView> getPostById(@PathVariable Long id) {

        PostView postView = postService.getPostById(id);
        if (postView != null) {
            return new ResponseEntity<>(postView, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


}
