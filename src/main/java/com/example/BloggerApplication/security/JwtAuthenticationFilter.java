package com.example.BloggerApplication.security;

import com.example.BloggerApplication.entites.User;
import com.example.BloggerApplication.exception.TokenExpiredException;
import com.example.BloggerApplication.services.JwtService;
import com.example.BloggerApplication.services.UserDetailsServiceImpl;
import com.example.BloggerApplication.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    final String header = "Authorization";

    final String prefix = "Bearer ";

    final int beginIndex = 7;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(header);

        if (authHeader == null || !authHeader.startsWith(prefix)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(beginIndex);

        try {
            final String username = jwtService.extractUsername(jwt);
            System.out.println(username);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


            if (username != null && authentication == null) {
                UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (TokenExpiredException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(ex.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}

