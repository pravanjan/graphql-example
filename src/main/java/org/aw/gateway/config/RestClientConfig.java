package org.aw.gateway.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;



@Configuration
public class RestClientConfig {


    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder()
                         .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                         .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    @Bean
    public RestClient jsonPlaceholderRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://jsonplaceholder.typicode.com")
                .defaultHeader("User-Agent", "GraphQL-Aggregator/1.0")
                .build();
    }


}

