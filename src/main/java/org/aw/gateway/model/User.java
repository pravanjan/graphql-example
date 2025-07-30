package org.aw.gateway.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
}
