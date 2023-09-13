package com.stm.DTO;

import com.stm.util.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
@Schema(description = "DTO for response")
public class ResponseDTO {
    @Schema(description = "Message from server")
    private String message;
    @Schema(description = "Timestamp of a response")
    private LocalDateTime timestamp;

    @Schema(description = "Error code for a response",
    oneOf = ErrorCode.class)
    private int errorCode;

    public ResponseDTO(String message, LocalDateTime timestamp, ErrorCode errorCode) {
        this.message = message;
        this.timestamp = timestamp;
        this.errorCode = errorCode.getCode();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
