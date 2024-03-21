package io.github.danielmkraus.applicationinsights.aop;

public class TraceableCall {

    private final Invocation callable;
    private final String methodName;
    private final String signature;

    public TraceableCall(Invocation callable, String methodName, String signature) {
        this.callable = callable;
        this.methodName = methodName;
        this.signature = signature;
    }

    public Object proceed() throws Throwable {
        return callable.proceed();
    }

    public String getMethodName() {
        return methodName;
    }

    public String getSignature() {
        return signature;
    }
}
