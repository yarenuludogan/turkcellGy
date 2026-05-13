package com.turkcell.spring_cqrs.core.security.authorization;

import java.util.Set;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.core.exception.AuthorizationException;
import com.turkcell.spring_cqrs.core.exception.UnauthenticatedException;
import com.turkcell.spring_cqrs.core.mediator.pipeline.PipelineBehavior;
import com.turkcell.spring_cqrs.core.mediator.pipeline.RequestHandlerDelegate;
import com.turkcell.spring_cqrs.core.security.context.UserContext;

@Component
@Order(10)
public class AuthorizationBehavior implements PipelineBehavior {
    private final UserContext userContext;

    public AuthorizationBehavior(UserContext userContext) {
        this.userContext = userContext;
    }

    @Override
    public boolean supports(Object request) {
        return request instanceof AuthorizableRequest;
    }

    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        if (!userContext.isAuthenticated()) {
            throw new UnauthenticatedException("You need to log in");
        }

        AuthorizableRequest authz = (AuthorizableRequest) request;
        Set<String> required = authz.requiredRoles();
        if (!required.isEmpty()) {
            boolean hasRole = userContext.getRoles().stream().anyMatch(required::contains);
            if (!hasRole) {
                throw new AuthorizationException("You do not have authorization");
            }
        }

        return next.invoke();
    }
}
