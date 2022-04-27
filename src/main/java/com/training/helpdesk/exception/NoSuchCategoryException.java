package com.training.helpdesk.exception;

public class NoSuchCategoryException extends RuntimeException {
    public NoSuchCategoryException(String message) {
        super(message);
    }
}
