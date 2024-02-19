package io.github.danielmkraus.applicationinsights.configuration;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.Duration;
import com.microsoft.applicationinsights.telemetry.RemoteDependencyTelemetry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;

import java.util.Date;

public class ApplicationInsightsTracker {

    private final TelemetryClient telemetryClient;
    private final String dependencyType;
    private final ClassExecutionFilter classExecutionFilter;

    public ApplicationInsightsTracker(TelemetryClient telemetryClient, String dependencyType, ClassExecutionFilter classExecutionFilter) {
        this.telemetryClient = telemetryClient;
        this.dependencyType = dependencyType;
        this.classExecutionFilter = classExecutionFilter;
    }

    public Object trackCall(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (!classExecutionFilter.filter(proceedingJoinPoint.getSignature().getDeclaringTypeName())) {
            return proceedingJoinPoint.proceed();
        }

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
