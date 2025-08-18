package org.aw.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aw.gateway.model.Post;
import org.aw.gateway.model.User;
import org.aw.gateway.model.UserWithPosts;
import org.aw.gateway.services.PostService;
import org.aw.gateway.services.UserService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class GraphQLController {
    private final UserService userService;
    private final PostService postService;

    @QueryMapping
    public User user(@Argument Long id) {
        log.info("Fetching user with id: {}", id);
        return userService.getUserById(id).block();
    }

    @QueryMapping
    public List<Post> posts() {
        return postService.getAllPosts(20);
    }

    @QueryMapping
    public List<User> users() {
        log.info("GraphQL query: users");
        return userService.getAllUsers().collectList().block();
    }

    @QueryMapping
    public Post post(@Argument Long id) {
        log.info("GraphQL query: post(id: {})", id);
        return postService.getPostById(id);
    }

    @MutationMapping
    @Secured("SCOPE_ADMIN")
    public Post createPost(@Argument PostInput postInput) {
        log.info("GraphQL mutation: createPost(input: {})", postInput);
        return postService.createPost(postInput);
    }

    @QueryMapping
    public UserWithPosts getUserWithPosts(@Argument Long userId) {
        log.info("Fetching user with posts for userId: {}", userId);
        User user = null;
        List<Post> userPosts = null;

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var userFuture = executor.submit(() -> userService.getUserById(userId));
            var postsFuture = executor.submit(() -> postService.getPostsByUserId(userId));
            user = userFuture.get().block();
            userPosts = postsFuture.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int totalComments = userPosts.stream().map(post -> post.getComments() != null ? post.getComments().size() : 0)
                                     .reduce(0, Integer::sum);


        return new UserWithPosts(user, userPosts, userPosts.size(), totalComments);
    }

}
