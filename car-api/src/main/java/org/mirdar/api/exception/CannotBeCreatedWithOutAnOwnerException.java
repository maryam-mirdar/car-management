package org.mirdar.api.exception;

public class CannotBeCreatedWithOutAnOwnerException extends RuntimeException {

    public CannotBeCreatedWithOutAnOwnerException() {
        super("A car cannot be created without an owner.");
    }
}
