package com.stm.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Schema(description = "Entity to filter available tickets")
public class FilterRequestDTO {
    @Schema(description = "Date from when we need to search")
    @NotNull(message = "startDateTime cannot be empty.")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", timezone = "Europe/Moscow")
    private Date startDateTime;

    @Schema(description = "Date before which we need to search")
    @NotNull(message = "endDateTime cannot be empty.")
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm", timezone = "Europe/Moscow")
    private Date endDateTime;

    @Schema(description = "Departure Point to filter")
    @NotEmpty(message = "Departure Point cannot be empty.")
    private String departurePoint;

    @Schema(description = "Destination Point to filter")
    @NotEmpty(message = "Destination Point cannot be empty.")
    private String destinationPoint;

    @Schema(description = "Transporter title to filter")
    @NotEmpty(message = "Transporter title cannot be empty.")
    private String transporter;

    public FilterRequestDTO() {
    }

    public FilterRequestDTO(Date startDateTime, Date endDateTime, String departurePoint, String destinationPoint, String transporter) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.departurePoint = departurePoint;
        this.destinationPoint = destinationPoint;
        this.transporter = transporter;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
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

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }
}
