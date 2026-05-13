package com.turkcell.spring_cqrs.core.security.authorization;

import java.util.Collections;
import java.util.Set;

public interface AuthorizableRequest {

    /**
     * Boş ise sadece kimlik doğrulama yeterlidir. Dolu ise kullanıcının rollerinden
     * en az biri bu kümede bulunmalıdır.
     */
    default Set<String> requiredRoles() {
        return Collections.emptySet();
    }
}
