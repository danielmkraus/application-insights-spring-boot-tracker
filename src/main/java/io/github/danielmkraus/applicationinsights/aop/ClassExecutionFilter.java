package io.github.danielmkraus.applicationinsights.aop;

import org.springframework.util.AntPathMatcher;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

public class ClassExecutionFilter {

    private final AntPathMatcher matcher;
    private final List<String> packagesIncluded;
    private final List<String> packagesExcluded;

    public ClassExecutionFilter(AntPathMatcher matcher, List<String> includePackages, List<String> excludePackages) {
        this.packagesIncluded = includePackages;
        this.packagesExcluded = excludePackages;
        this.matcher = matcher;
    }

    public boolean matches(Class<?> clazz) {
        return filter(clazz.getCanonicalName());
    }

    public boolean filter(String invocation) {
        boolean notExcluded = isEmpty(packagesExcluded) ||
                packagesExcluded.stream()
                        .noneMatch(pattern -> matcher.match(pattern, invocation));

        return notExcluded && included(invocation);
    }

    private boolean included(String invocation) {
        return isEmpty(packagesIncluded)
                || packagesIncluded.stream()
                .anyMatch(pattern -> matcher.match(pattern, invocation));
    }
}
