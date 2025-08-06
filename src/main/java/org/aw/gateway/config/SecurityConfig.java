package org.aw.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Configure security filter chain here
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity, adjust as needed
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Allow all requests, adjust as needed
                )
                .build();
}
