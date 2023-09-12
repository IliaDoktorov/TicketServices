package com.stm.TicketService.services;

import com.stm.TicketService.DAO.RouteDAO;
import com.stm.TicketService.DAO.TransporterDAO;
import com.stm.TicketService.models.Route;
import com.stm.TicketService.util.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RouteService {
    private final RouteDAO routeDAO;
    private final TransporterDAO transporterDAO;

    @Autowired
    public RouteService(RouteDAO routeDAO, TransporterDAO transporterDAO) {
        this.routeDAO = routeDAO;
        this.transporterDAO = transporterDAO;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(Route route){
        if(transporterDAO.existById(route.getTransporter()).isEmpty())
            throw new RequestException("Transporter with provided id not found");

        routeDAO.add(route);
    }

    public void update(int id, Route route){
        if(id == 0 || (routeDAO.existById(id).isEmpty()))
            throw new RequestException("Route with provided id not found");

        if(transporterDAO.existById(route.getTransporter()).isEmpty())
            throw new RequestException("Transporter with provided id not found");

        route.setId(id);

        routeDAO.update(route);
    }

    public void delete(int id){
        if(id == 0 || (routeDAO.existById(id).isEmpty()))
            throw new RequestException("Route with provided id not found");

        routeDAO.delete(id);
    }
}
