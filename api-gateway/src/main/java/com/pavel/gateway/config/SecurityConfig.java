package com.pavel.gateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200")); // Tu Angular
                corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                corsConfiguration.setAllowCredentials(true);
                return corsConfiguration;
            }))
            .authorizeExchange(exchange -> exchange
                // 1. Permitir OPTIONS para que no salga error de CORS en navegador
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                
                // 2. Rutas publicas
                .pathMatchers("/api/login/**", "/oauth2/**", "/admin/**").permitAll()
                
                // 3. Reglas de tu compañero (Admins borran, usuarios ven/editan)
                .pathMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                .pathMatchers(HttpMethod.GET, "/**").hasAnyRole("ADMIN", "USER")
                .pathMatchers(HttpMethod.POST, "/**").hasAnyRole("ADMIN", "USER")
                .pathMatchers(HttpMethod.PUT, "/**").hasAnyRole("ADMIN", "USER")
                
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->
                jwt.jwtAuthenticationConverter(reactiveJwtAuthenticationConverterAdapter())));
        
        return http.build();
    }
    
    // Este es el convertidor que usa tu compañero para leer los roles del Token
    @Bean
    ReactiveJwtAuthenticationConverterAdapter reactiveJwtAuthenticationConverterAdapter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); 
        
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}