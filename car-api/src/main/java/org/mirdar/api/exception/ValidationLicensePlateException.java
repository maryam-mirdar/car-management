package org.mirdar.api.exception;

public class ValidationLicensePlateException extends RuntimeException {
    public ValidationLicensePlateException(String message) {
        super("invalid licensePlate : " + message);
    }
}
