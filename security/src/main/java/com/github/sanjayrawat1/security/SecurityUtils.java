package com.github.sanjayrawat1.security;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for Spring Security.
 *
 * @author sanjayrawat1
 */
public final class SecurityUtils {

    public static final String EMPTY_SECURITY_CONTEXT_MESSAGE = "%s not present in security context";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String SYSTEM_CLIENT_ID = SYSTEM_ACCOUNT;

    private SecurityUtils() {}

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    public static Optional<String> getCurrentClientId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        AuthenticationDetails authenticationDetails = extractDetails(securityContext.getAuthentication());
        return Optional.ofNullable(authenticationDetails != null ? authenticationDetails.clientId() : null);
    }

    private static AuthenticationDetails extractDetails(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getDetails() instanceof AuthenticationDetails) {
            return (AuthenticationDetails) authentication.getDetails();
        }
        return null;
    }

    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional
            .ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    }

    public static boolean isCurrentUserInRole(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).anyMatch(authority::equals);
    }

    public static String getCustomerId() {
        return getCurrentUserLogin().orElseThrow(() -> new NoSuchElementException(String.format(EMPTY_SECURITY_CONTEXT_MESSAGE, "Customer id")));
    }

    public static String getClientId() {
        return getCurrentClientId().orElseThrow(() -> new NoSuchElementException(String.format(EMPTY_SECURITY_CONTEXT_MESSAGE, "Client id")));
    }

    public static void createAuthenticationForSystem(Object principal) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.SYSTEM));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
        authentication.setDetails(new AuthenticationDetails(SYSTEM_CLIENT_ID));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
