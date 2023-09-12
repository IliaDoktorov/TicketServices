package com.stm.TicketService.util;

public enum ErrorCode {
    DB_CONCURRENT_MODIFICATION_ERROR(405),
    VALIDATION_ERROR(400),
    ENTITY_NOT_FOUND_ERROR(404),
    SUCCESSFUL(200);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
