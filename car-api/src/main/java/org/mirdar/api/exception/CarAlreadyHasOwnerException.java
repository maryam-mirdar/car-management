package org.mirdar.api.exception;

public class CarAlreadyHasOwnerException extends RuntimeException {
    public CarAlreadyHasOwnerException() {
        super("This car already has an owner.");
    }
}
