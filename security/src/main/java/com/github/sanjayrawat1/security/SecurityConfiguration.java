package com.github.sanjayrawat1.security;

import com.github.sanjayrawat1.security.jwt.JWTConfigurer;
import com.github.sanjayrawat1.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

/**
 * Security Configuration.
 *
 * @author sanjayrawat1
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

    private final SecurityProblemSupport problemSupport;

    // prettier-ignore
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
			.disable()
			.exceptionHandling()
				.authenticationEntryPoint(problemSupport)
				.accessDeniedHandler(problemSupport)
		.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
			.authorizeRequests()
			.antMatchers("/spring-boot-multi-module/api/**").authenticated()
			.antMatchers("/management/health").permitAll()
			.antMatchers("/management/health/ping").permitAll()
			.antMatchers("/management/info").permitAll()
			.antMatchers("/management/prometheus").permitAll()
			.antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
		.and()
			.apply(securityConfigurerAdapter());
	}

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
