package io.github.danielmkraus.applicationinsights.aop;

import io.github.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking;
import io.github.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import io.github.danielmkraus.applicationinsights.configuration.ApplicationInsightsTracker;

/**
 * Aspect responsible for intercepting all targets with the {@link ApplicationInsightsTracking}
 * annotation and send the telemetry to azure application insights
 *
 * @see ApplicationInsightsTracking
 * @see DisableApplicationInsightsTracking
 */
@Aspect
public class AnnotationSpringBeanMethodCallInterceptor {

    private final ApplicationInsightsTracker applicationInsightsTracker;

    public AnnotationSpringBeanMethodCallInterceptor(ApplicationInsightsTracker applicationInsightsTracker) {
        this.applicationInsightsTracker = applicationInsightsTracker;
    }

    @Around("( @within(io.github.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking)" +
            "  || @target(io.github.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking)" +
            "  || @annotation(io.github.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking)" +
            ") && !@within(io.github.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) " +
            "&& !@target(io.github.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) " +
            "&& !@annotation(io.github.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) ")
    public Object intercept(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return applicationInsightsTracker.trackCall(proceedingJoinPoint);
    }
}
