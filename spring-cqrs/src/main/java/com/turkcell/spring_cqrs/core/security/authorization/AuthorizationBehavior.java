package com.turkcell.spring_cqrs.core.security.authorization;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.turkcell.spring_cqrs.core.exception.UnauthenticatedException;
import com.turkcell.spring_cqrs.core.exception.UnauthorizedException;
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


    // ilgili handler'ın öncesi ve sonrası çalıştırabilen kodlar.
    @Override
    public <R> R handle(Object request, RequestHandlerDelegate<R> next) {
        if(!userContext.isAuthenticated()) {
            throw new UnauthenticatedException("You need to log in");}

        if(!userContext.getRoles().equals("ADMIN")) {
            throw new UnauthorizedException("You do not have authorization");
        }    
        // Özel bir exception türü belirle.
        // Handlerda bu exceptionı eğer giriş yapılmamışsa 401, (UnauthenticatedException)
        // yapılmış ancak rol yetersiz ise 403 döndürecek şekilde (UnauthorizedException)
        // düzenle..
        
        return next.invoke(); // zincirdeki sonraki halkayı çağır..
    }

}
