package com.example.BloggerApplication.controllers;

import com.example.BloggerApplication.dto.PostDto;
import com.example.BloggerApplication.services.PostService;
import com.example.BloggerApplication.views.PostView;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<PostView> addPost(HttpServletRequest request , @Valid @RequestBody PostDto postDto) {

        PostView addedPost = postService.addPost(request , postDto);
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
        return new ResponseEntity<>(postView, HttpStatus.OK);

    }


    @PutMapping("/posts/{id}")
    public ResponseEntity<PostView> updatePost( HttpServletRequest request , @Valid @RequestBody PostDto postDto, @PathVariable Long id) {

        PostView postView =  postService.updatePost(request , id, postDto);
        return new ResponseEntity<>(postView , HttpStatus.OK);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<String> deletePost(HttpServletRequest request , @PathVariable Long id) {

        String reponse = postService.deletePost(request , id);
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }

}
