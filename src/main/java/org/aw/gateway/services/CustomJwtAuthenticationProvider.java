package org.aw.gateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomJwtAuthenticationProvider implements AuthenticationProvider {
    private final DemoAuthService demoAuthService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getCredentials());
        log.debug("Authenticating with token: {}", token);
        BearerTokenAuthenticationToken bearerToken = new BearerTokenAuthenticationToken(token);

        if (!StringUtils.hasText(token)) {
            throw new AuthenticationException("You have provided a empty token") {
            };

        }
        if (!token.startsWith("demo-")) {
            throw new AuthenticationException("You have provided a token that does not work ") {
            };
        }

        bearerToken.setAuthenticated(true);
        AuthUserDetail authUserDetail = demoAuthService.getAuthUser(token);
        bearerToken.setDetails(authUserDetail);

        return bearerToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAuthentication.class.isAssignableFrom(authentication);

    }
}
