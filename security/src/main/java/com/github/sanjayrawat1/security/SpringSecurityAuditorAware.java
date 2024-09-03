package com.github.sanjayrawat1.security;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Implementation of{@link AuditorAware} based on Spring Security.
 *
 * @author sanjayrawat1
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(SecurityUtils.SYSTEM_ACCOUNT));
    }
}
