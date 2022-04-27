package com.training.helpdesk.exception;

public class NoAccessToChangeTicketState extends RuntimeException {
    public NoAccessToChangeTicketState(String message) {
        super(message);
    }
}
