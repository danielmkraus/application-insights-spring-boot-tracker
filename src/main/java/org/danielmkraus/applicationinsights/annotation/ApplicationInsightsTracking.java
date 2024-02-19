package org.danielmkraus.applicationinsights.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Annotation which activates the application insights tracking.
 * Important to note that this annotation takes precedence over {@link ApplicationInsightsTracking}, which means, target
 * class or method annotated with both annotations will be deactivated.
 *
 * @see DisableApplicationInsightsTracking
 */
@Documented
@Target({ElementType.METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationInsightsTracking {
}
