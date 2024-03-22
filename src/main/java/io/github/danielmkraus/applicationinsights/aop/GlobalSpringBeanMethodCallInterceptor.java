package io.github.danielmkraus.applicationinsights.aop;

import io.github.danielmkraus.applicationinsights.configuration.ApplicationInsightsTracker;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;

/**
 * Responsible to intercept all spring bean calls to be tracked in azure application insights
 */
public class GlobalSpringBeanMethodCallInterceptor implements Advisor, MethodInterceptor {

    private final ApplicationInsightsTracker tracker;

    public GlobalSpringBeanMethodCallInterceptor(ApplicationInsightsTracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public Advice getAdvice() {
        return this;
    }

    @Override
    public boolean isPerInstance(){
        return true;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return tracker.trackCall(new TraceableCall(invocation::proceed,
                getShortMethodName(invocation),
                invocation.getMethod().toGenericString()
        ));
    }

    private static String getShortMethodName(MethodInvocation invocation) {
        return invocation.getClass().getSimpleName() + "." + invocation.getMethod().getName();
    }

}

