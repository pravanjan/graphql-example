package org.aw.gateway.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.Time)
                .scalar(ExtendedScalars.Url)
                .scalar(ExtendedScalars.UUID)
                .scalar(ExtendedScalars.PositiveInt)
                .scalar(ExtendedScalars.NegativeInt)
                .scalar(ExtendedScalars.NonNegativeInt)
                .scalar(ExtendedScalars.Currency)
                .scalar(ExtendedScalars.Locale);
    }
}
