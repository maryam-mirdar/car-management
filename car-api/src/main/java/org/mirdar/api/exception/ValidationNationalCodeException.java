package org.mirdar.api.exception;

public class ValidationNationalCodeException extends RuntimeException {

    public ValidationNationalCodeException(String message) {
        super("invalid nationalCode : " + message);
    }
}