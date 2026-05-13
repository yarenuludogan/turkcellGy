package com.turkcell.spring_cqrs.application.features.category.query.getall;

import java.util.Set;

import org.springframework.data.domain.Page;

import com.turkcell.spring_cqrs.core.mediator.cqrs.Query;
import com.turkcell.spring_cqrs.core.security.authorization.AuthorizableRequest;

public record GetAllCategoriesQuery(int pageNumber, int pageSize)
        implements Query<Page<GetAllCategoriesResponse>>, AuthorizableRequest {

    @Override
    public Set<String> requiredRoles() {
        return Set.of("ADMIN");
    }
}
