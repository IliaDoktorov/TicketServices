package com.stm.controllers;

import com.stm.DTO.FilterRequestDTO;
import com.stm.DTO.ReserveDTO;
import com.stm.DTO.ResponseDTO;
import com.stm.kafka.KafkaProducer;
import com.stm.models.Ticket;
import com.stm.repository.RedisRepositoryImpl;
import com.stm.services.TicketService;
import com.stm.util.ErrorCode;
import com.stm.util.FilterValidator;
import com.stm.util.RequestException;
import com.stm.util.TicketValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Tag(name = "TicketController", description = "RestController for creating, updating, removing and reservation of tickets")
@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final TicketValidator ticketValidator;
    private final FilterValidator filterValidator;
    private final KafkaProducer kafkaProducer;

    @Autowired
    public TicketController(TicketService ticketService, TicketValidator ticketValidator, FilterValidator filterValidator, KafkaProducer kafkaProducer) {
        this.ticketService = ticketService;
        this.ticketValidator = ticketValidator;
        this.filterValidator = filterValidator;
        this.kafkaProducer = kafkaProducer;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Ticket creation", description = "Allows to create new ticket")
    @PostMapping
    public ResponseEntity<ResponseDTO> add(@RequestBody @Valid Ticket ticket,
                                           BindingResult bindingResult) {
        ticketValidator.validate(ticket, bindingResult);

        ticketService.add(ticket);

        return ResponseEntity.ok(new ResponseDTO("Ticket was successfully added", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Ticket modification", description = "Allows to create update a ticket")
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid Ticket ticket,
                                              BindingResult bindingResult,
                                              @PathVariable("id") @Parameter(description = "Identifier of a ticket", required = true) int id) {
        ticketValidator.validate(ticket, bindingResult);

        ticketService.update(id, ticket);

        return ResponseEntity.ok(new ResponseDTO("Ticket was successfully updated", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Ticket deletion", description = "Allows to remove a ticket")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") @Parameter(description = "Identifier of a ticket", required = true) int id) {
        ticketService.delete(id);

        return ResponseEntity.ok(new ResponseDTO("Ticket was successfully deleted", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
    }

    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Ticket reservation", description = "Allows to reserve a ticket for a specified user")
    @PatchMapping(value = "/reserve")
    public ResponseEntity<ResponseDTO> reserve(@RequestBody ReserveDTO reserveDTO) {
        ticketService.reserve(reserveDTO.getCustomerId(), reserveDTO.getTicketId());

        System.out.println("Ticket reserved");
        Optional<Ticket> ticket = ticketService.getById(reserveDTO.getTicketId());

        System.out.println("Ticket retrieved");

        kafkaProducer.notifyKafka(ticket.get());

        return ResponseEntity.ok(new ResponseDTO("Ticket was successfully reserved", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
    }

    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Available tickets", description = "Allows to get ticket that available to reservation")
    @PostMapping("/available")
    public List<Ticket> listAvailable(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                      @RequestParam(value = "items_per_page", required = false, defaultValue = "10") int itemsPerPage,
                                      @RequestBody @Valid FilterRequestDTO filterRequestDTO,
                                      BindingResult bindingResult) {
        filterValidator.validate(filterRequestDTO, bindingResult);

        return ticketService.available(page, itemsPerPage, filterRequestDTO);
    }

    @ExceptionHandler
    private ResponseEntity<ResponseDTO> handleException(RequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(ex.getMessage(), LocalDateTime.now(), ErrorCode.VALIDATION_ERROR));
    }

    @ExceptionHandler
    private ResponseEntity<ResponseDTO> handleException(MissingServletRequestParameterException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(ex.getMessage(), LocalDateTime.now(), ErrorCode.VALIDATION_ERROR));
    }

    // Separate handler for JSON exceptions, mostly used to send back DateTime format errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<ResponseDTO> handleException(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(ex.getMessage(), LocalDateTime.now(), ErrorCode.VALIDATION_ERROR));
    }

    // Separate handler for DB concurrent issues.
    // Any logic could be implemented here, but for now just notify a client about the issue.
    // And ask to re-try.
    @ExceptionHandler(SQLException.class)
    private ResponseEntity<ResponseDTO> handleException(SQLException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO("Something went wrong, please try again", LocalDateTime.now(), ErrorCode.DB_CONCURRENT_MODIFICATION_ERROR));
    }
}
