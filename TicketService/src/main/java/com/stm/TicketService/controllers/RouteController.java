package com.stm.TicketService.controllers;

import com.stm.TicketService.DTO.ResponseDTO;
import com.stm.TicketService.models.Route;
import com.stm.TicketService.services.RouteService;
import com.stm.TicketService.util.ErrorCode;
import com.stm.TicketService.util.RequestException;
import com.stm.TicketService.util.RouteValidator;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Tag(name = "RouteController", description = "RestController for creating, updating and removing of routes")
@RestController
@RequestMapping("/routes")
public class RouteController {
    private final RouteService routeService;
    private final RouteValidator routeValidator;

    @Autowired
    public RouteController(RouteService routeService, RouteValidator routeValidator) {
        this.routeService = routeService;
        this.routeValidator = routeValidator;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Route creation", description = "Allows to create new route")
    @PostMapping
    public ResponseEntity<ResponseDTO> add(@RequestBody @Valid Route route,
                                           BindingResult bindingResult) {
        routeValidator.validate(route, bindingResult);

        routeService.add(route);

        return ResponseEntity.ok(new ResponseDTO("Route was successfully added", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Route modification", description = "Allows to update a route")
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid Route route,
                                              BindingResult bindingResult,
                                              @PathVariable("id") @Parameter(description = "Identifier of a route", required = true) int id) {
        routeValidator.validate(route, bindingResult);

        routeService.update(id, route);

        return ResponseEntity.ok(new ResponseDTO("Route was successfully updated", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Route deletion", description = "Allows to remove a route")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") @Parameter(description = "Identifier of a route", required = true) int id) {
        routeService.delete(id);

        return ResponseEntity.ok(new ResponseDTO("Route was successfully deleted", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
    }

    @ExceptionHandler
    private ResponseEntity<ResponseDTO> handleException(RequestException ex) {
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
