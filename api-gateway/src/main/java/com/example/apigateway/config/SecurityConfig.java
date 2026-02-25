package com.example.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

/**
 * Relax security for the API Gateway (reactive) so it can proxy requests publicly during
 * local development / demo. Adjust rules later for production.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.csrf().disable()
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll() // allow everything through the gateway
            )
            .httpBasic().disable()
            .formLogin().disable();

        return http.build();
    }
}
