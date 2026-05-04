package com.example.librarycqrs.application.mediator;

public interface RequestHandler<TRequest extends Request<TResponse>, TResponse> {

    Class<TRequest> requestType();

    TResponse handle(TRequest request);
}
