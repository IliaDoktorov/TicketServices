package com.stm.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Ticket implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int id;

    private int route;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm", timezone = "Europe/Moscow")
    private Date dateTime;

    private int price;

    private int placeNumber;

    private int reservedBy;

    public Ticket() {
    }

    public Ticket(int id, int route, Date dateTime, int price, int reservedBy, int placeNumber) {
        this.id = id;
        this.route = route;
        this.dateTime = dateTime;
        this.price = price;
        this.reservedBy = reservedBy;
        this.placeNumber = placeNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoute() {
        return route;
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(int reservedBy) {
        this.reservedBy = reservedBy;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return route == ticket.route && price == ticket.price && placeNumber == ticket.placeNumber && reservedBy == ticket.reservedBy && Objects.equals(dateTime, ticket.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, dateTime, price, placeNumber, reservedBy);
    }
}
