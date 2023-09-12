package com.stm.TicketService.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Schema(description = "Transporter entity")
public class Transporter {
    @Schema(description = "Transporter identifier",
    accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Schema(description = "Title of a transporter",
    minLength = 3, maxLength = 30)
    @NotEmpty(message = "Title cannot be empty")
    @Size(min = 3, max = 30, message = "Title should have 3-30 symbols.")
    private String title;

    @Schema(description = "Phone number of a transporter",
            minLength = 10, maxLength = 20)
    @NotEmpty(message = "Phone number cannot be empty")
    @Size(min = 10, max = 20, message = "Phone number should have 10-20 symbols.")
    private String phoneNumber;

    public Transporter() {
    }

    public Transporter(int id, String title, String phoneNumber) {
        this.id = id;
        this.title = title;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transporter that = (Transporter) o;
        return Objects.equals(title, that.title) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, phoneNumber);
    }
}
