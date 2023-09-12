package com.stm.TicketService.services;

import com.stm.TicketService.DAO.RouteDAO;
import com.stm.TicketService.DAO.TicketDAO;
import com.stm.TicketService.DAO.UserDAO;
import com.stm.TicketService.DTO.FilterRequestDTO;
import com.stm.TicketService.models.Ticket;
import com.stm.TicketService.util.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {
    private final TicketDAO ticketDAO;
    private final RouteDAO routeDAO;
    private final UserDAO userDAO;

    @Autowired
    public TicketService(TicketDAO ticketDAO, RouteDAO routeDAO, UserDAO userDAO) {
        this.ticketDAO = ticketDAO;
        this.routeDAO = routeDAO;
        this.userDAO = userDAO;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(Ticket ticket){
        // make sure route exist
        if(ticket.getRoute() == 0 || routeDAO.existById(ticket.getRoute()).isEmpty())
            throw new RequestException("Route with provided id not found");

        // make sure placeNumber is unique per route
        if(ticketDAO.existByRouteAndPlaceNumber(ticket.getRoute(), ticket.getPlaceNumber()).isPresent())
            throw new RequestException("Ticket with such place number and route already exist");

        ticketDAO.add(ticket);
    }

    public void update(int id, Ticket ticket){
        if(id == 0 || ticketDAO.getById(id).isEmpty())
            throw new RequestException("Ticket with provided id not found");

        // make sure route exist
        if(ticket.getRoute() == 0 || routeDAO.existById(ticket.getRoute()).isEmpty())
            throw new RequestException("Route with provided id not found");

        // make sure placeNumber is unique per route
        if(ticketDAO.existByRouteAndPlaceNumber(ticket.getRoute(), ticket.getPlaceNumber()).isPresent())
            throw new RequestException("Ticket with such place number and route already exist");

        ticket.setId(id);

        ticketDAO.update(ticket);
    }

    public void delete(int id){
        if(id == 0 || ticketDAO.getById(id).isEmpty())
            throw new RequestException("Ticket with provided id not found");

        ticketDAO.delete(id);
    }

    public void reserve(int customerId, int ticketId){
        Optional<Ticket> ticket = ticketDAO.getById(ticketId);

        if(ticketId == 0 || ticket.isEmpty())
            throw new RequestException("Ticket with provided id not found");

        //check that ticket is vacant
        if(ticket.get().getReservedBy() != 0)
            throw new RequestException("Ticket with provided id already reserved");

        if(customerId == 0 || userDAO.getById(customerId).isEmpty())
            throw new RequestException("User with provided id not found");

        ticketDAO.reserve(customerId, ticketId);
    }

    public Optional<Ticket> getById(int id){
        return ticketDAO.getById(id);
    }

    public List<Ticket> available(int page, int itemsPerPage, FilterRequestDTO filterRequestDTO) {

        return ticketDAO.available(page, itemsPerPage, filterRequestDTO);
    }
}
