package com.github.sanjayrawat1.security.jwt;

import com.github.sanjayrawat1.config.ApplicationProperties;
import com.github.sanjayrawat1.security.AuthenticationDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * JWT token provider.
 *
 * @author sanjayrawat1
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    public static final String AUTHORITIES_KEY = "auth";

    public static final String ISSUER = "spring-boot-multi-module";

    private final ApplicationProperties applicationProperties;

    private final Set<String> requiredClaims = Set.of("iss", "sub", "aud", "iat", AUTHORITIES_KEY);

    public boolean validateToken(String authToken, String clientId) {
        var clientDetails = applicationProperties.getSecurity().getAuthentication().getClients().get(clientId);
        try {
            validateClient(clientDetails);
            Claims claims = Jwts.parserBuilder().setSigningKey(getKey(clientId)).build().parseClaimsJws(authToken).getBody();
            validateClaims(claims);
            validateIssuer(claims);
            validateTokenValidity(claims, clientDetails);
            validateAudience(claims, clientId);
            validateAuthorities(claims, clientDetails);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", ex);
        }
        return false;
    }

    public Authentication getAuthentication(String token, String clientId) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getKey(clientId)).build().parseClaimsJws(token).getBody();
        List<? extends GrantedAuthority> authorities = Arrays
            .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, token, authorities);
        authentication.setDetails(new AuthenticationDetails(clientId));
        return authentication;
    }

    private Key getKey(String client) {
        var clients = applicationProperties.getSecurity().getAuthentication().getClients();
        String secret = clients.get(client).getSecret();
        byte[] keyBytes = getKeyBytes(secret, clients.get(client));
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private byte[] getKeyBytes(String secret, ApplicationProperties.Security.Authentication.Jwt jwt) {
        byte[] keyBytes;
        if (!ObjectUtils.isEmpty(secret)) {
            log.warn(
                "Warning: the JWT key used not Base64-encoded. We recommend using the `application.security.authentication.clients.hwt.base64-secret` key for optimum security."
            );
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key.");
            keyBytes = Decoders.BASE64.decode(jwt.getBase64Secret());
        }
        return keyBytes;
    }

    private void validateClient(ApplicationProperties.Security.Authentication.Jwt clientDetails) {
        if (clientDetails == null) throw new JwtException("Invalid client.");
    }

    private void validateClaims(Claims claims) {
        if (!claims.keySet().containsAll(requiredClaims)) throw new IllegalArgumentException("Missing required claims.");
    }

    private void validateIssuer(Claims claims) {
        if (!claims.getIssuer().equals(ISSUER)) throw new JwtException("Invalid issuer.");
    }

    private void validateTokenValidity(Claims claims, ApplicationProperties.Security.Authentication.Jwt clientDetails) {
        long issuedAt = claims.getIssuedAt().getTime();
        long tokenValidityInMilliseconds = 1000 * clientDetails.getTokenValidityInSeconds();
        Date validity = new Date(issuedAt + tokenValidityInMilliseconds);
        if (System.currentTimeMillis() > validity.getTime()) throw new JwtException("JWT expired.");
    }

    private void validateAudience(Claims claims, String clientId) {
        if (!clientId.equals(claims.getAudience())) throw new JwtException("Invalid audience.");
    }

    private void validateAuthorities(Claims claims, ApplicationProperties.Security.Authentication.Jwt clientDetails) {
        Set<String> authorities = claims.get(AUTHORITIES_KEY) != null
            ? Arrays.stream(claims.get(AUTHORITIES_KEY, String.class).split(",")).map(String::new).collect(Collectors.toSet())
            : new HashSet<>();
        if (!authorities.containsAll(clientDetails.getRoles())) throw new JwtException("Invalid authorities");
    }
}
