package com.ragbecca.renegaderesource.config;

import com.ragbecca.renegaderesource.token.BearerTokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class JWTSecurityConfig {

    private final BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter = new BearerTokenAuthenticationFilter();
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and()
                .authorizeRequests().requestMatchers("/character").hasAnyRole("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers("/test").hasAnyRole("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers("/test2").permitAll()
                .anyRequest().authenticated().and().addFilterBefore(bearerTokenAuthenticationFilter, org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter.class)
                .oauth2ResourceServer().jwt().decoder(jwtDecoder());

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).jwsAlgorithm(SignatureAlgorithm.RS512).build();
    }
}