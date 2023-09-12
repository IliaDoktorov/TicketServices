package com.stm.TicketService.controllers;

import com.stm.TicketService.DTO.RefreshTokenRequestDTO;
import com.stm.TicketService.DTO.ResponseDTO;
import com.stm.TicketService.models.RefreshToken;
import com.stm.TicketService.models.User;
import com.stm.TicketService.security.JWTUtil;
import com.stm.TicketService.security.PersonDetails;
import com.stm.TicketService.services.RefreshTokenService;
import com.stm.TicketService.services.UserService;
import com.stm.TicketService.util.ErrorCode;
import com.stm.TicketService.util.RefreshTokenException;
import com.stm.TicketService.util.RequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshTokenService refreshTokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Optional<RefreshToken> refreshTokenFromDB = refreshTokenService.getByUserId(personDetails.getUser().getId());
        refreshTokenFromDB.ifPresent(refreshTokenService::delete);

        String accessToken = jwtUtil.generateToken(user.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(personDetails.getUser().getId());

        return ResponseEntity.ok(Map.of("access-token", accessToken, "refresh-token", refreshToken.getToken()));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        String refreshToken = refreshTokenRequestDTO.getRefreshToken();

        Optional<RefreshToken> refreshTokenFromDB = refreshTokenService.findByToken(refreshToken);

        if (refreshTokenFromDB.isEmpty())
            throw new RefreshTokenException("RefreshToken not found");

//        return refreshTokenFromDB
//                .map(refreshTokenService::checkExpiration)
//                .map(RefreshToken::getUserId)
//                .map(userId -> userService.getByid(userId).map(User::getUsername))
//                .flatMap(s -> s
//                        .map(username -> {
//                            String accessToken = jwtUtil.generateToken(username);
//                            return ResponseEntity.ok(Map.of("access-token", accessToken, "refresh-token", refreshTokenFromDB.get().getToken()));
//                        })).get();

        RefreshToken validatedToken = refreshTokenService.checkExpiration(refreshTokenFromDB.get());
        Optional<User> user = userService.getByid(validatedToken.getUserId());

        if(user.isEmpty())
            throw new RequestException("User not found");

        String accessToken = jwtUtil.generateToken(user.get().getUsername());

        return ResponseEntity.ok(Map.of("access-token", accessToken, "refresh-token", refreshTokenFromDB.get().getToken()));
    }

    @ExceptionHandler
    private ResponseEntity<ResponseDTO> handleException(RequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(ex.getMessage(), LocalDateTime.now(), ErrorCode.VALIDATION_ERROR));
    }

    @ExceptionHandler
    private ResponseEntity<ResponseDTO> handleException(RefreshTokenException ex) {
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
