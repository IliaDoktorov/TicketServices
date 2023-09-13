package com.stm.controllers;

import com.stm.DTO.ResponseDTO;
import com.stm.models.Transporter;
import com.stm.services.TransporterService;
import com.stm.util.ErrorCode;
import com.stm.util.RequestException;
import com.stm.util.TransporterValidator;
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

@Tag(name = "TransporterController", description = "RestController for creating, updating and removing of transporters")
@RestController
@RequestMapping("/transporters")
public class TransporterController {
    private final TransporterService transporterService;
    private final TransporterValidator transporterValidator;

    @Autowired
    public TransporterController(TransporterService transporterService, TransporterValidator transporterValidator) {
        this.transporterService = transporterService;
        this.transporterValidator = transporterValidator;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Transporter creation", description = "Allows to create new transporter")
    @PostMapping
    public ResponseEntity<ResponseDTO> add(@RequestBody @Valid Transporter transporter,
                                           BindingResult bindingResult) {
        transporterValidator.validate(transporter, bindingResult);

        transporterService.add(transporter);

        return ResponseEntity.ok(new ResponseDTO("Transporter was successfully added", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Ticket modification", description = "Allows to update a transporter")
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO> update(@RequestBody @Valid Transporter transporter,
                                              BindingResult bindingResult,
                                              @PathVariable("id") @Parameter(description = "Identifier of a transporter", required = true) int id) {
        transporterValidator.validate(transporter, bindingResult);

        transporterService.update(id, transporter);

        return ResponseEntity.ok(new ResponseDTO("Transporter was successfully updated", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = ResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Operation(summary = "Transporter deletion", description = "Allows to remove a transporter")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable("id") @Parameter(description = "Identifier of a transporter", required = true) int id) {
        transporterService.delete(id);

        return ResponseEntity.ok(new ResponseDTO("Transporter was successfully deleted", LocalDateTime.now(), ErrorCode.SUCCESSFUL));
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
