package com.stm.config;

import com.stm.security.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final AuthenticationManager authenticationManager;
    private final JWTFilter jwtFilter;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfig(AuthenticationManager authenticationManager, JWTFilter jwtFilter, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.authenticationManager = authenticationManager;
        this.jwtFilter = jwtFilter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/people/register", "/auth/login", "/auth/refreshtoken").permitAll()
//                        .requestMatchers("/routes").hasAuthority("ROLE_ADMIN")
//                        .requestMatchers("/transporters").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.accessDeniedHandler(customAccessDeniedHandler))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
