package org.danielmkraus.applicationinsights.configuration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DependencyTrackerInterceptorConfiguration.class)
public @interface EnableApplicationInsightsDependencyTracer {
}
