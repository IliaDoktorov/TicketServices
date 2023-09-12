package com.stm.TicketService.controllers;

import com.stm.TicketService.DTO.ResponseDTO;
import com.stm.TicketService.models.Ticket;
import com.stm.TicketService.models.User;
import com.stm.TicketService.services.UserService;
import com.stm.TicketService.util.ErrorCode;
import com.stm.TicketService.util.RequestException;
import com.stm.TicketService.util.UserValidator;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "UserController", description = "RestController for creating users and getting tickets per user")
@RestController
@RequestMapping("/people")
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @Operation(summary = "User registration", description = "Allows to register new user")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody @Valid User user,
                                                BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        userService.register(user);

        return ResponseEntity.ok(new ResponseDTO("User registered successfully", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
    }

    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = List.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Tickets of user", description = "Retrieve all tickets for specified user")
    @GetMapping("/{id}/tickets")
    public List<Ticket> tickets(@PathVariable("id") @Parameter(description = "Identifier of a user", required = true) int id) {

        return userService.tickets(id);
    }

    @ExceptionHandler(RequestException.class)
    private ResponseEntity<ResponseDTO> handleException(RequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(ex.getMessage(), LocalDateTime.now(), ErrorCode.VALIDATION_ERROR));
    }

    /**
     * Since we might have a situation when two concurrent request with same username comes in
     * we need to set SERIALIZABLE for register transactions and have to notify our client about the issue.
     * We fail request and respond back with usual "Something went wrong, please try again" message,
     * so when client would try again, he will get a message - such username already taken.
     */
    @ExceptionHandler(SQLException.class)
    private ResponseEntity<ResponseDTO> handleException(SQLException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO("Something went wrong, please try again", LocalDateTime.now(), ErrorCode.DB_CONCURRENT_MODIFICATION_ERROR));
    }
}
