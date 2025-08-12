package org.aw.gateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomJwtAuthenticationProvider implements AuthenticationProvider {
    private final DemoAuthService demoAuthService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getCredentials());
        AuthUserDetail authUserDetail = demoAuthService.getAuthUser(token);
        Collection<GrantedAuthority> authorities = loadAuthorities(authUserDetail);
        log.debug("Authenticating with authorities {} token: {}", authorities, token);

        UsernamePasswordAuthenticationToken bearerToken = new UsernamePasswordAuthenticationToken(authUserDetail, token, authorities);

        if (!StringUtils.hasText(token)) {
            throw new AuthenticationException("You have provided a empty token") {
            };

        }
        if (!token.startsWith("demo-")) {
            throw new AuthenticationException("You have provided a token that does not work ") {
            };
        }

        bearerToken.setDetails(authUserDetail);

        return bearerToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAuthentication.class.isAssignableFrom(authentication);

    }

    private Collection<GrantedAuthority> loadAuthorities(AuthUserDetail authUserDetail) {

        // Convert roles to GrantedAuthority
        Set<String> allRoles = new HashSet<>(authUserDetail.roles());

        return allRoles.stream()
                       .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                       .collect(Collectors.toList());
    }
}
