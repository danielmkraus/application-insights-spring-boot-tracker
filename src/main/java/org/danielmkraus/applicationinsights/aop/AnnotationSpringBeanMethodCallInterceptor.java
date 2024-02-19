package org.danielmkraus.applicationinsights.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.danielmkraus.applicationinsights.configuration.ApplicationInsightsTracker;

/**
 * Aspect responsible for intercepting all targets with the {@link org.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking}
 * annotation and send the telemetry to azure application insights
 *
 * @see org.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking
 * @see org.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking
 */
@Aspect
public class AnnotationSpringBeanMethodCallInterceptor {

    private final ApplicationInsightsTracker applicationInsightsTracker;

    public AnnotationSpringBeanMethodCallInterceptor(ApplicationInsightsTracker applicationInsightsTracker) {
        this.applicationInsightsTracker = applicationInsightsTracker;
    }

    @Around("( @within(org.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking)" +
            "  || @target(org.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking)" +
            "  || @annotation(org.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking)" +
            ") && !@within(org.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) " +
            "&& !@target(org.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) " +
            "&& !@annotation(org.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) ")
    public Object intercept(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return applicationInsightsTracker.trackCall(proceedingJoinPoint);
    }
}
