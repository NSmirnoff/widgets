package org.widget.exception;

public class RequiredEntityNotFoundException extends RuntimeException {

    public RequiredEntityNotFoundException(String message) {
        super(message);
    }

    public RequiredEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
