package com.example.BloggerApplication.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginResponse {

    private String token;

    private Long expiresIn;
}
