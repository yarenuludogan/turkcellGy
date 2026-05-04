package com.example.librarycqrs.application.mediator;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SimpleMediator implements Mediator {

    private final Map<Class<?>, RequestHandler<?, ?>> handlersByRequestType;

    public SimpleMediator(List<RequestHandler<?, ?>> handlers) {
        this.handlersByRequestType = handlers.stream()
                .collect(Collectors.toMap(RequestHandler::requestType, Function.identity()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TResponse> TResponse send(Request<TResponse> request) {
        RequestHandler<Request<TResponse>, TResponse> handler =
                (RequestHandler<Request<TResponse>, TResponse>) handlersByRequestType.get(request.getClass());

        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for " + request.getClass().getName());
        }

        return handler.handle(request);
    }
}
