package io.github.danielmkraus.applicationinsights.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Disables the application insights tracking for the annotated target.
 * Important to note that this annotation takes precedence over {@link ApplicationInsightsTracking}, which means, target
 * class or method annotated with both annotations will be deactivated.
 *
 * @see ApplicationInsightsTracking
 */
@Documented
@Target({METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DisableApplicationInsightsTracking {
}
