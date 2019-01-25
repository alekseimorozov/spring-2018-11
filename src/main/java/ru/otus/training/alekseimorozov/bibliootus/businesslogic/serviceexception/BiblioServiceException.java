package ru.otus.training.alekseimorozov.bibliootus.businesslogic.serviceexception;

/**
 * wrap all checked exception
 */
public class BiblioServiceException extends RuntimeException {
    public BiblioServiceException(String message) {
        super(message);
    }

    public BiblioServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}