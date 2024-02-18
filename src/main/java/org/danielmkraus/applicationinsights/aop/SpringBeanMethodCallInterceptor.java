package org.danielmkraus.applicationinsights.aop;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.Duration;
import com.microsoft.applicationinsights.telemetry.RemoteDependencyTelemetry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.danielmkraus.applicationinsights.configuration.ClassExecutionFilter;

import java.util.Date;

@Aspect
public class SpringBeanMethodCallInterceptor {

    private final TelemetryClient telemetryClient;
    private final ClassExecutionFilter classExecutionFilter;
    private final String dependencyType;

    public SpringBeanMethodCallInterceptor(TelemetryClient telemetryClient, ClassExecutionFilter filter, String dependencyType) {
        this.telemetryClient = telemetryClient;
        this.classExecutionFilter = filter;
        this.dependencyType = dependencyType;
    }

    @Around("within(*) " +
            "&& !within(org.springframework.context..*) " +
            "&& !within(org.danielmkraus.applicationinsights..*) " +
            "&& !within(org.springframework.beans..*) " +
            "&& !within(org.springframework.boot..*)" +
            "&& !within(com.microsoft.applicationinsights..*)")
    public Object intercept(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (classExecutionFilter.filter(proceedingJoinPoint.getSignature().getDeclaringTypeName())) {
            return trackCall(proceedingJoinPoint);
        }
        return proceedingJoinPoint.proceed();
    }

    private Object trackCall(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Date start = new Date();
        boolean succeed = true;
        try {
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            succeed = false;
            throw e;
        } finally {
            RemoteDependencyTelemetry dependencyTelemetry = new RemoteDependencyTelemetry();
            dependencyTelemetry.setDuration(new Duration(System.currentTimeMillis() - start.getTime()));
            dependencyTelemetry.setType(dependencyType);
            Signature signature = proceedingJoinPoint.getSignature();
            dependencyTelemetry.setName(signature.toShortString());
            dependencyTelemetry.setCommandName(signature.toLongString());
            dependencyTelemetry.setTimestamp(start);
            dependencyTelemetry.setSuccess(succeed);
            telemetryClient.trackDependency(dependencyTelemetry);
        }
    }
}

