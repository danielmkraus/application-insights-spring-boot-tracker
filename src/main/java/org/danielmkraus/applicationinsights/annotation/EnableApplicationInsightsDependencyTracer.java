package org.danielmkraus.applicationinsights.annotation;

import org.danielmkraus.applicationinsights.configuration.DependencyTrackerInterceptorConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Enabler annotation for the Azure Application Insights Spring Bean dependency tracker.
 *
 * @see DisableApplicationInsightsTracking
 * @see ApplicationInsightsTracking
 * @see org.danielmkraus.applicationinsights.configuration.DependencyTrackerInterceptorConfigurationProperties
 */
@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DependencyTrackerInterceptorConfiguration.class)
public @interface EnableApplicationInsightsDependencyTracer {
}
