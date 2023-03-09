package ru.practicum.shareit.exceptions;

public class ErrorResponse {
    private final Integer code;
    private final String error;

    public ErrorResponse(Integer code, String error) {
        this.code = code;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public Integer getCode() {
        return code;
    }
}
