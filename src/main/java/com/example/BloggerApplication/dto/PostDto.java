package com.example.BloggerApplication.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    @NotBlank
    String title;
    @NotBlank
    String content;
}
