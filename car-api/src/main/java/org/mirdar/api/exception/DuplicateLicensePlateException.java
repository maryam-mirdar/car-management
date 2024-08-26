package org.mirdar.api.exception;

public class DuplicateLicensePlateException extends RuntimeException{

    public DuplicateLicensePlateException(String message) {
        super("duplicate licensePlate : " + message);
    }
}
