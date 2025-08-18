package org.aw.gateway.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomJwtAuthenticationProvider implements AuthenticationProvider {
    private final DemoAuthService demoAuthService;
    private final SecurityUtils securityUtils;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = String.valueOf(authentication.getCredentials());


        if (!StringUtils.hasText(token)) {
            throw new AuthenticationException("You have provided a empty token") {
            };

        }
        if (!token.startsWith("demo-")) {
            throw new AuthenticationException("You have provided a token that does not work ") {
            };
        }

        // 3) Build principal (no sub required)
        Map<String, Object> attrs = new HashMap<>();
        var demoUser = demoAuthService.getAuthUser(token);
        attrs.put("token_type", "server");
        attrs.put("display_name", "Demo User");
        // You can also include a synthetic name you can reference in logs
        var principal = new DefaultOAuth2AuthenticatedPrincipal(attrs, securityUtils.getAuthorities(demoUser.roles()));

        // 4) Wrap token (optionally set iat/exp if you know them; otherwise nulls are ok)
        var auth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, token, Instant.now(), null);

        // 5) Return authenticated Authentication
        return new BearerTokenAuthentication(principal, auth2AccessToken, securityUtils.getAuthorities(demoUser.roles()));

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAuthentication.class.isAssignableFrom(authentication);

    }

}
