package org.aw.gateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aw.gateway.model.User;
import org.aw.gateway.services.utils.FeedUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final RestClient jsonPlaceholderRestClient;
    private final FeedUtils feedUtils;

    public Mono<User> getUserById(Long userId) {
        return Mono.fromCallable(() -> {
                       log.info("Fetching user with ID: {}", userId);

                       return jsonPlaceholderRestClient
                               .get()
                               .uri("/users/{id}", userId)
                               .retrieve()
                               .body(User.class);
                   })
                   .onErrorMap(RestClientException.class, e ->
                           new RuntimeException("Failed to fetch user: " + e.getMessage(), e));
    }

    public Flux<User> getAllUsers() {
        return Mono.fromCallable(() -> {
                       List<User> users = jsonPlaceholderRestClient
                               .get()
                               .uri("/users")
                               .retrieve()
                               .body(new ParameterizedTypeReference<List<User>>() {
                               });

                       return users != null ? users : List.<User>of();
                   })
                   .flatMapMany(Flux::fromIterable);
    }

    public List<Map<String, Object>> getUserFeed(Long userId, String fields) {

        Set<String> requestedFields = feedUtils.parseFields(fields);

        if (!feedUtils.validateFields(requestedFields)) {
            return null;
        }

        return feedUtils.filterFeedData(getUserFeedData(), requestedFields);
    }

    public List<Map<String, Object>> getUserFeedData() {
        List<Map<String, Object>> feedList = new ArrayList<>();

        try {
            Map<String, Object> feedItem1 = new HashMap<>();
            feedItem1.put("id", 1L);
            feedItem1.put("email", "john.doe@example.com");
            feedItem1.put("name", "John Doe");
            feedItem1.put("postName", "Introduction to Spring Boot");
            feedItem1.put("postContent", "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications.");
            feedItem1.put("comment", "Great tutorial! Very helpful for beginners.");
            feedItem1.put("commentedUser", "Jane Smith");
            feedItem1.put("timestamp", LocalDateTime.now().minusHours(2));
            feedItem1.put("likes", 45);
            feedItem1.put("shares", 12);
            feedList.add(feedItem1);

            Map<String, Object> feedItem2 = new HashMap<>();
            feedItem2.put("id", 2L);
            feedItem2.put("email", "alice.johnson@example.com");
            feedItem2.put("name", "Alice Johnson");
            feedItem2.put("postName", "REST API Best Practices");
            feedItem2.put("postContent", "Learn how to design RESTful APIs that are scalable and maintainable.");
            feedItem2.put("comment", "This helped me improve my API design significantly!");
            feedItem2.put("commentedUser", "Bob Wilson");
            feedItem2.put("timestamp", LocalDateTime.now().minusHours(5));
            feedItem2.put("likes", 78);
            feedItem2.put("shares", 23);
            feedList.add(feedItem2);

            Map<String, Object> feedItem3 = new HashMap<>();
            feedItem3.put("id", 3L);
            feedItem3.put("email", "mike.brown@example.com");
            feedItem3.put("name", "Mike Brown");
            feedItem3.put("postName", "Microservices Architecture Guide");
            feedItem3.put("postContent", "A comprehensive guide to building microservices with Spring Cloud.");
            feedItem3.put("comment", "Excellent breakdown of complex concepts!");
            feedItem3.put("commentedUser", "Sarah Davis");
            feedItem3.put("timestamp", LocalDateTime.now().minusDays(1));
            feedItem3.put("likes", 156);
            feedItem3.put("shares", 45);
            feedList.add(feedItem3);


        } catch (RestClientException e) {
            log.error("Error fetching user feed data: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch user feed data", e);
        }
        return feedList;
    }


}
