package com.training.helpdesk.exception;

public class NoAccessToTicketException extends RuntimeException {
    public NoAccessToTicketException(String message) {
        super(message);
    }
}
