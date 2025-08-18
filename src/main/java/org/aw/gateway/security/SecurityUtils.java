package org.aw.gateway.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class SecurityUtils {
    public List<GrantedAuthority> getAuthorities(Set<String> scopes) {
        return scopes == null
                ? List.of()
                : scopes.stream().map(s -> (GrantedAuthority) new SimpleGrantedAuthority("SCOPE_" + s)).toList();
    }
}
