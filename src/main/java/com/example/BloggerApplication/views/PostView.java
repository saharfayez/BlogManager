package com.example.BloggerApplication.views;

import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostView {

    Long id;

    String title;

    String content;

    String userName;
}