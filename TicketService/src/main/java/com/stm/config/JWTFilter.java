package com.stm.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.stm.security.JWTUtil;
import com.stm.security.PersonDetails;
import com.stm.services.PersonDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    private final PersonDetailsService personDetailsService;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in Bearer Header");
            } else {
                try {
                    System.out.println("Before validation");
                    String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                    System.out.println("After validation");

                    PersonDetails personDetails = null;

                    personDetails = (PersonDetails) personDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            personDetails,
                            personDetails.getPassword(),
                            personDetails.getAuthorities()
                    );

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (JWTVerificationException ex) {
                    System.out.println("before JWTVerificationException");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
                    System.out.println("after JWTVerificationException");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
