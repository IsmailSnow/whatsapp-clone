package com.example.whatsappsclone.core.security;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Stream<GrantedAuthority> grantedAuthorityStream = jwtGrantedAuthoritiesConverter.convert(jwt).stream();
        Stream<GrantedAuthority> authorityStream = extractResourceRoles(jwt).stream();
        var authorities = Stream.concat(
                grantedAuthorityStream,
                authorityStream).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaim("preferred_username"));
    }

    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        return Optional.ofNullable(jwt)
                .map(item -> (Map<String, Object>) item.getClaim("realm_access"))
                .map(item -> (Collection<String>) item.get("roles"))
                .orElse(Set.of())
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
