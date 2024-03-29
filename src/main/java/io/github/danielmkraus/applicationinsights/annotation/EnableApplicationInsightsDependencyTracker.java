package io.github.danielmkraus.applicationinsights.annotation;

import io.github.danielmkraus.applicationinsights.configuration.DependencyTrackerInterceptorConfiguration;
import io.github.danielmkraus.applicationinsights.configuration.DependencyTrackerInterceptorConfigurationProperties;
import io.github.danielmkraus.applicationinsights.configuration.DependencyTrackerInterceptorRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Enabler annotation for the Azure Application Insights Spring Bean dependency tracker.
 *
 * @see DependencyTrackerInterceptorConfigurationProperties
 */
@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({
        DependencyTrackerInterceptorRegistrar.class,
        DependencyTrackerInterceptorConfiguration.class
})
public @interface EnableApplicationInsightsDependencyTracker {
}
