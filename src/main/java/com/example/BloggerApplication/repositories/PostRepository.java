package com.example.BloggerApplication.repositories;

import com.example.BloggerApplication.entites.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  PostRepository extends JpaRepository<Post, Long> {

}
