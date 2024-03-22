package io.github.danielmkraus.applicationinsights.configuration;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.Duration;
import com.microsoft.applicationinsights.telemetry.RemoteDependencyTelemetry;
import io.github.danielmkraus.applicationinsights.aop.TraceableCall;

import java.util.Date;

public class ApplicationInsightsTracker {

    private final TelemetryClient telemetryClient;
    private final String dependencyType;

    public ApplicationInsightsTracker(TelemetryClient telemetryClient, String dependencyType) {
        this.telemetryClient = telemetryClient;
        this.dependencyType = dependencyType;
    }

    public Object trackCall(TraceableCall trackedCall) throws Throwable {
        Date start = new Date();
        boolean succeed = true;
        try {
            return trackedCall.proceed();
        } catch (Throwable e) {
            succeed = false;
            throw e;
        } finally {
            RemoteDependencyTelemetry dependencyTelemetry = new RemoteDependencyTelemetry();
            dependencyTelemetry.setDuration(new Duration(System.currentTimeMillis() - start.getTime()));
            dependencyTelemetry.setType(dependencyType);
            dependencyTelemetry.setName(trackedCall.getMethodName());
            dependencyTelemetry.setCommandName(trackedCall.getSignature());
            dependencyTelemetry.setTimestamp(start);
            dependencyTelemetry.setSuccess(succeed);
            telemetryClient.trackDependency(dependencyTelemetry);
        }
    }
}
