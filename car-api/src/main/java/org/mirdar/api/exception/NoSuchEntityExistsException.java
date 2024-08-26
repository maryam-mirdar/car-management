package org.mirdar.api.exception;

public class NoSuchEntityExistsException extends RuntimeException {

    public NoSuchEntityExistsException(String entity, Long message) {
        super(entity + " with id = " + message + " does not exist.");
    }
}
