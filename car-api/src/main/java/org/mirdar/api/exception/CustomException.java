package org.mirdar.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final String message;
    private final HttpStatus status;
    private final String code;

    public CustomException(String message, HttpStatus status, String code) {
        this.message = message;
        this.status = status;
        this.code = code;
    }
}