package org.aw.gateway.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aw.gateway.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final RestClient jsonPlaceholderRestClient;

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

}
