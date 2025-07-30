package org.aw.gateway.model;

import lombok.Data;

import java.util.List;

@Data
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String body;
    private List<Comment> comments;
}
