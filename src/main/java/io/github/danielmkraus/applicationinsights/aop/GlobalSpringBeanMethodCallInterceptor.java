package io.github.danielmkraus.applicationinsights.aop;

import io.github.danielmkraus.applicationinsights.configuration.ApplicationInsightsTracker;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

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
            "&& !within(io.github.danielmkraus.applicationinsights..*) " +
            "&& !within(is(EnumType)) " +
            "&& !within(is(FinalType)) " +
            "&& !within(is(AspectType))" +
            "&& !within(org.springframework..*) " +
            "&& !@within(io.github.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking) " +
            "&& !@within(io.github.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) " +
            "&& !@target(io.github.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) " +
            "&& !@annotation(io.github.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking) " +
            "&& !within(com.microsoft.applicationinsights..*) ")
    public Object intercept(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return tracker.trackCall(proceedingJoinPoint);
    }
}

