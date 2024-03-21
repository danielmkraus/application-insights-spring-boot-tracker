package io.github.danielmkraus.applicationinsights.aop;

@FunctionalInterface
public interface Invocation {
    Object proceed() throws Throwable;
}
