package com.github.sanjayrawat1.config;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metrics configuration.
 *
 * @author Sanjay Singh Rawat
 */
@Configuration
public class MetricsConfiguration {

    /**
     * AspectJ aspect for intercepting types or method annotated with @Timed.
     * By default, @Timed annotation does not work with method execution,
     * so we have to enable TimeAspect which used AspectJ for intercepting method annotation.
     *
     * @param registry default meter registry.
     * @return TimedAspect timed aspect.
     */
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    /**
     * AspectJ aspect for interception types or method annotated with @Counted.
     * By default, @Counted annotation does not work with method execution,
     * so we have to enable CountedAspect which used AspectJ for interception method annotation.
     *
     * @param registry default meter registry.
     * @return CountedAspect counted aspect.
     */
    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }
}
