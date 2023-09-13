package com.stm.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Schema(description = "Route entity")
public class Route {
    @Schema(description = "Identifier of route",
    accessMode = Schema.AccessMode.READ_ONLY)
    private int id;
    @Schema(description = "Departure Point",
    minLength = 3, maxLength = 30)
    @Size(min = 3, max = 30, message = "Departure point should have 3-30 symbols.")
    private String departurePoint;

    @Schema(description = "Destination Point",
            minLength = 3, maxLength = 30)
    @Size(min = 3, max = 30, message = "Destination point should have 3-30 symbols.")
    private String destinationPoint;

    @Schema(description = "Transporter identifier")
    @Min(value = 1, message = "Transporter cannot be 0 or negative")
    private int transporter;

    @Schema(description = "Travel time")
    @Positive(message = "Travel time cannot be 0 or negative")
    private int travelTime;

    public Route() {
    }

    public Route(int id, String departurePoint, String destinationPoint, int transporter, int travelTime) {
        this.id = id;
        this.departurePoint = departurePoint;
        this.destinationPoint = destinationPoint;
        this.transporter = transporter;
        this.travelTime = travelTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeparturePoint() {
        return departurePoint;
    }

    public void setDeparturePoint(String departurePoint) {
        this.departurePoint = departurePoint;
    }

    public String getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(String destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public int getTransporter() {
        return transporter;
    }

    public void setTransporter(int transporter) {
        this.transporter = transporter;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return transporter == route.transporter && travelTime == route.travelTime && Objects.equals(departurePoint, route.departurePoint) && Objects.equals(destinationPoint, route.destinationPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departurePoint, destinationPoint, transporter, travelTime);
    }
}
