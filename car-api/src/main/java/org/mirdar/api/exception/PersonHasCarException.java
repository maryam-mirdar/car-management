package org.mirdar.api.exception;

public class PersonHasCarException extends RuntimeException {
    public PersonHasCarException() {
        super("Cannot delete person.The person is the owner of one or more cars.");
    }
}
