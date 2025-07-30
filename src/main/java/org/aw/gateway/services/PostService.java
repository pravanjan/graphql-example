package org.aw.gateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aw.gateway.model.Comment;
import org.aw.gateway.model.Post;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final RestClient jsonPlaceholderRestClient;

    public Post getPostById(Long id) {
        try {
            Post post = jsonPlaceholderRestClient
                    .get()
                    .uri("/posts/{id}", id)
                    .retrieve()
                    .body(Post.class);

            if (post != null) {
                enrichPostWithComments(post);
            }
            return post;
        } catch (RestClientException e) {
            log.error("Error fetching post with id {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to fetch post with id " + id, e);
        }
    }

    private void enrichPostWithComments(Post post) {
        try {
            List<Comment> comments = getCommentsByPostId(post.getId());
            post.setComments(comments);
        } catch (Exception e) {
            log.warn("Failed to fetch comments for post {}: {}", post.getId(), e.getMessage());
            post.setComments(new ArrayList<>());
        }
    }
    private List<Comment> getCommentsByPostId(Long postId) {
        try {
            List<Comment> comments = jsonPlaceholderRestClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/comments")
                            .queryParam("postId", postId)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            return comments != null ? comments : new ArrayList<>();
        } catch (RestClientException e) {
            log.error("Error fetching comments for post {}: {}", postId, e.getMessage());
            return new ArrayList<>();
        }
    }


    public List<Post> getAllPosts(Integer limit) {
        try {
            List<Post> posts = jsonPlaceholderRestClient
                    .get()
                    .uri("/posts")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            if (posts != null) {
                posts.forEach(this::enrichPostWithComments);

                // Apply limit if specified
                if (limit != null && limit > 0 && posts.size() > limit) {
                    posts = posts.subList(0, limit);
                }
            }

            return posts != null ? posts : new ArrayList<>();
        } catch (RestClientException e) {
            log.error("Error fetching all posts: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch all posts", e);
        }
    }
    public List<Post> getPostsByUserId(Long userId) {
        try {
            List<Post> posts = jsonPlaceholderRestClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/posts")
                            .queryParam("userId", userId)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Post>>() {});

            if (posts != null) {
                posts.forEach(this::enrichPostWithComments);
            }

            return posts != null ? posts : new ArrayList<>();
        } catch (RestClientException e) {
            log.error("Error fetching posts for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to fetch posts for user " + userId, e);
        }
    }
}
