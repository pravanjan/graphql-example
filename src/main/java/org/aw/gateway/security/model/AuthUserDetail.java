package org.aw.gateway.security.model;

import java.util.Set;

public record AuthUserDetail(String userId, String accountId, Set<String> roles) {
}
