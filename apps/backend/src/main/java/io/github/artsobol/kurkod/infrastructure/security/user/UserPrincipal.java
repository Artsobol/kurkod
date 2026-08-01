package io.github.artsobol.kurkod.infrastructure.security.user;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public record UserPrincipal(
        long userId, String username, Collection<? extends GrantedAuthority> authorities
) {
}
