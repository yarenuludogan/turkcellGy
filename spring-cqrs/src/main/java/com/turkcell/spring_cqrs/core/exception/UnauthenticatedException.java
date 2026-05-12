package com.turkcell.spring_cqrs.core.exception;

public class UnauthenticatedException
        extends RuntimeException {

    public UnauthenticatedException(String message) {
        super(message);
    }
}