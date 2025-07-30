package org.aw.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
public class UserWithPosts {
    private User user;
    private List<Post> posts;
    private int totalPosts;
    private int totalComments;
}
