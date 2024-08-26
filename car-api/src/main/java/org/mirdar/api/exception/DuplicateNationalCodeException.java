package org.mirdar.api.exception;

public class DuplicateNationalCodeException extends RuntimeException{

    public DuplicateNationalCodeException(String message) {
        super("duplicate nationalCode : " + message);
    }
}