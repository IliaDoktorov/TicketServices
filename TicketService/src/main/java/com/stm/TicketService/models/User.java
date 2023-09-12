package com.stm.TicketService.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Objects;

@Schema(description = "User entity")
public class User {
    @Schema(description = "User identifier",
    accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Schema(description = "Username of a user")
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Schema(description = "Password of a user")
    @NotNull
    private String password;

    @Schema(description = "User initials",
    pattern = "[A-Z]\\w+ [A-Z]\\w+ [A-Z]\\w+",
    example = "Ivanov Ivan Ivanich")
    @Size(min = 3, max = 30, message = "Initials should have 3-30 symbols.")
    @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+ [A-Z]\\w+", message = "Initials should be separated by space and starts with capital letter.")
    private String initials;

    private String role;

    public User() {
    }

    public User(int id, String username, String password, String initials, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.initials = initials;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
