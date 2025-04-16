package com.example.whatsappsclone.core.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.*;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>.JwtConfigurer> configurerCustomizer = token -> token.jwtAuthenticationConverter(new JwtConverter());
        Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> serverConfigurerCustomizer = auth -> auth.jwt(configurerCustomizer);
        Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsCustomizer = req -> req.requestMatchers("/ws/**","/auth/*").permitAll().anyRequest().authenticated();
        httpSecurity.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer)
                .oauth2ResourceServer(serverConfigurerCustomizer);
        return httpSecurity.build();
    }


    @Bean
    public CorsFilter corsFilter(@Value("#{'${cors.allowedOrigins}'.split(',')}") List<String> allowedOrigins,
                                 @Value("#{'${cors.allowedMethods}'.split(',')}") List<String> allowedMethods){
        final var source = new UrlBasedCorsConfigurationSource();
        final var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(allowedOrigins);
        final var allowedHeaders = Arrays.asList(HttpHeaders.ORIGIN, HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT, HttpHeaders.AUTHORIZATION);
        config.setAllowedHeaders(allowedHeaders);
        config.setAllowedMethods(allowedMethods);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
