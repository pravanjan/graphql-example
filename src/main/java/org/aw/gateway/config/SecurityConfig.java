package org.aw.gateway.config;

import lombok.RequiredArgsConstructor;
import org.aw.gateway.services.CustomAuthenticationEntryPoint;
import org.aw.gateway.services.CustomJwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomJwtAuthenticationProvider customJwtAuthenticationProvider;

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return new CustomAuthenticationEntryPoint(resolver);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        // Configure security filter chain here

        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity, adjust as needed
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/graphiql").permitAll()

                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        // custom auth provider based on full auth service
                        .jwt(jwt -> jwt.authenticationManager(customJwtAuthenticationProvider::authenticate))
                        .withObjectPostProcessor(
                                new ObjectPostProcessor<BearerTokenAuthenticationFilter>() {
                                    @Override
                                    public <T extends BearerTokenAuthenticationFilter> T postProcess(T filter) {
                                        filter.setAuthenticationFailureHandler(authenticationEntryPoint::commence);
                                        return filter;
                                    }
                                }))
                .exceptionHandling(configure -> configure.authenticationEntryPoint(authenticationEntryPoint))
                .build();
    }


}
