package com.example.BloggerApplication.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RequestResponse {

        private Long id;

        private String username;

}
