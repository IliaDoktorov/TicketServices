package com.stm.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

import java.util.Date;
import java.util.Objects;

@Schema(description = "Ticket entity")
public class Ticket {
    @Schema(description = "Ticket identifier",
    accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Schema(description = "Route identifier")
    private int route;

    // use hardcoded timezone and dateTime format
//    @Schema(description = "Date and Time of departure",
//    pattern = "dd.MM.yyyy HH:mm",
//    example = "10.09.2023 13:23")
//    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", timezone = "Europe/Moscow")
    @Schema(description = "Date and Time of departure",
            pattern = "yyyy.MM.dd HH:mm",
            example = "2023.09.20 13:23")
    @JsonFormat(pattern = "yyyy.MM.dd HH:mm", timezone = "Europe/Moscow")
    private Date dateTime;

    @Schema(description = "Price of a ticket")
    @Positive(message = "Price cannot be 0 or negative")
    private int price;

    @Schema(description = "Place to seat")
    @Positive(message = "Place number cannot be 0 or negative")
    private int placeNumber;

    @Schema(description = "Identifier of a customer who reserved a ticket",
    nullable = true)
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
