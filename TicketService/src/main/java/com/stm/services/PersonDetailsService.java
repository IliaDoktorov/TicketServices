package com.stm.services;

import com.stm.DAO.UserDAO;
import com.stm.models.User;
import com.stm.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final UserDAO userDAO;

    @Autowired
    public PersonDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDAO.findByUsername(username);

        if(user.isEmpty())
            throw new UsernameNotFoundException("Username not found");

        return new PersonDetails(user.get());
    }
}
