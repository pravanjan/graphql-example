package org.aw.gateway.services;

import java.util.Set;

public record AuthUserDetail(String userId, String accountId, Set<String> roles) {
}
