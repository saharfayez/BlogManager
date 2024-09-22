package com.example.BloggerApplication.config;

import com.example.BloggerApplication.exception.AuthExceptionHandler;
import com.example.BloggerApplication.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthExceptionHandler authExceptionHandler;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http.csrf(csrf->csrf.disable())
                        .cors(cors -> cors.configurationSource(corsConfigurationSource))
                        .authorizeHttpRequests(authorizeHttpRequest->{
                            authorizeHttpRequest.requestMatchers("/auth/**").permitAll()
                                    .anyRequest().authenticated();
                        })
                        .exceptionHandling(exception->exception.authenticationEntryPoint(authExceptionHandler))
                        .sessionManagement(session ->{
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                        })
                        .authenticationProvider(authenticationProvider)
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }
}
