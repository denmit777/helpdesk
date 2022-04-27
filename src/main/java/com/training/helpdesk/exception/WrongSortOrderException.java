package com.training.helpdesk.exception;

public class WrongSortOrderException extends RuntimeException {
    public WrongSortOrderException(String message) {
        super(message);
    }
}
