package com.stm.services;

import com.stm.DAO.TransporterDAO;
import com.stm.models.Transporter;
import com.stm.util.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

// use default isolation level(READ_COMMITED for Postgres)
// concurrent access are handled by SERIALIZABLE for registration requests
// and by "select for update" for update/delete requests
@Service
@Transactional
public class TransporterService {
    private final TransporterDAO transporterDAO;

    @Autowired
    public TransporterService(TransporterDAO transporterDAO) {
        this.transporterDAO = transporterDAO;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(Transporter transporter){
        if (transporterDAO.existByPhoneNumber(transporter.getPhoneNumber()).isPresent())
            throw new RequestException("Transporter with provided phone number already exist");

        transporterDAO.add(transporter);
    }

    public void update(int id, Transporter transporter){
        if (id == 0 || (transporterDAO.existById(id).isEmpty()))
            throw new RequestException("Transporter with provided id not found");

        // need to make that phone number is corresponding with id
        Optional<Transporter> fromDB = transporterDAO.existByPhoneNumber(transporter.getPhoneNumber());
        if (fromDB.isPresent() && fromDB.get().getId() != id) {
            throw new RequestException("Transporter with provided phone number already exist");
        }

        transporter.setId(id);

        transporterDAO.update(transporter);
    }

    public void delete(int id){
        if (id == 0 || (transporterDAO.existById(id).isEmpty()))
            throw new RequestException("Transporter with provided id not found");

        transporterDAO.delete(id);
    }
}
