package org.aw.gateway.model;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long postId;
    private String name;
    private String email;
    private String body;
}
