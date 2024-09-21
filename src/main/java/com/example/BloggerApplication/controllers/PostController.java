package com.example.BloggerApplication.controllers;

import com.example.BloggerApplication.dto.PostDto;
import com.example.BloggerApplication.services.PostService;
import com.example.BloggerApplication.views.PostView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostView> addPost(@Valid @RequestBody PostDto postDto) {

        PostView addedPost = postService.addPost(postDto);

        return new ResponseEntity<>(addedPost, HttpStatus.CREATED);
    }


}
