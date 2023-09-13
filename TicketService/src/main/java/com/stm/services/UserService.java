package com.stm.services;

import com.stm.DAO.TicketDAO;
import com.stm.DAO.UserDAO;
import com.stm.models.Ticket;
import com.stm.models.User;
import com.stm.util.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// use default isolation level(READ_COMMITTED for Postgres)
// concurrent access are handled by SERIALIZABLE for select query
// and by "select for update" for update/delete requests
@Transactional
@Service
public class UserService {
private final UserDAO userDAO;
private final TicketDAO ticketDAO;

    @Autowired
    public UserService(UserDAO userDAO, TicketDAO ticketDAO) {
        this.userDAO = userDAO;
        this.ticketDAO = ticketDAO;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void register(User user){
        if(userDAO.findByUsername(user.getUsername()).isPresent())
            throw new RequestException("User with provided username already exist");

        userDAO.register(user);
    }

    public List<Ticket> tickets(int id){
        if(id == 0 || userDAO.getById(id).isEmpty())
            throw new RequestException("User with provided id not found");

        return ticketDAO.listByCustomerId(id);
    }

    public Optional<User> getByid(int id){
        return userDAO.getById(id);
    }
}
