package com.stm.security;

import com.stm.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthManager implements AuthenticationManager {
    private final PersonDetailsService personDetailsService;

    @Autowired
    public AuthManager(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails personDetails = personDetailsService.loadUserByUsername(username);

        if(!password.equals(personDetails.getPassword()))
            throw new BadCredentialsException("Wrong password");

        return new UsernamePasswordAuthenticationToken(personDetails, password, Collections.emptyList());
    }
}
