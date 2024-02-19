package org.danielmkraus.applicationinsights.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.danielmkraus.applicationinsights.configuration.ApplicationInsightsTracker;

/**
 * Aspect responsible to intercept all spring bean calls to be tracked in azure application insights
 */
@Aspect
public class GlobalSpringBeanMethodCallInterceptor {

    private final ApplicationInsightsTracker tracker;

    public GlobalSpringBeanMethodCallInterceptor(ApplicationInsightsTracker tracker) {
        this.tracker = tracker;
    }

    @Around("within(*) " +
            "&& !within(org.danielmkraus.applicationinsights..*) " +
            "&& !within(is(EnumType)) " +
            "&& !within(is(FinalType)) " +
            "&& !within(is(AspectType))" +
            "&& !within(org.springframework..*) " +
            "&& !@within(org.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking) " +
            "&& !@within(org.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) " +
            "&& !@target(org.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) " +
            "&& !@annotation(org.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) " +
            "&& !within(com.microsoft.applicationinsights..*) ")
    public Object intercept(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return tracker.trackCall(proceedingJoinPoint);
    }
}

