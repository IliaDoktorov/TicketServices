package com.stm.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Entity to reserve a ticket for a specified user")
public class ReserveDTO {
    @Schema(description = "Customer identifier")
    private int customerId;

    @Schema(description = "Ticket identifier")
    private int ticketId;

    public ReserveDTO() {
    }

    public ReserveDTO(int customerId, int ticketId) {
        this.customerId = customerId;
        this.ticketId = ticketId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }
}
