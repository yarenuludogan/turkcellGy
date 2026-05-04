package com.example.librarycqrs.application.mediator;

public interface Mediator {
    <TResponse> TResponse send(Request<TResponse> request);
}
