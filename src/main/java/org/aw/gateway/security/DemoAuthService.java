package org.aw.gateway.security;

import org.aw.gateway.security.model.AuthUserDetail;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DemoAuthService {

    public AuthUserDetail getAuthUser(String token) {

        if (token.startsWith("demo-admin")) {

            return new AuthUserDetail("admin", "accountId", Set.of("ADMIN"));
        } else {
            return new AuthUserDetail("user", "accountId", Set.of("USER"));
        }
    }

    public String getTokenType() {
        return "server";
    }


}
