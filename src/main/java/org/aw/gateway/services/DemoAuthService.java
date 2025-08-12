package org.aw.gateway.services;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DemoAuthService {

    public AuthUserDetail getAuthUser(String token) {

        if (token.startsWith("demo-admin")) {

            return new AuthUserDetail("admin", "accountId", Set.of("ADMIN_ROLE"));
        }
        if (token.startsWith("demo-user")) {
            return new AuthUserDetail("user", "accountId", Set.of("USER_ROLE"));
        }
        return null;
    }


}
