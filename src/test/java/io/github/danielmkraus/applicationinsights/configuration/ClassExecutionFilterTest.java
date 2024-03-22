package io.github.danielmkraus.applicationinsights.configuration;

import io.github.danielmkraus.applicationinsights.aop.ClassExecutionFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.util.AntPathMatcher;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

class ClassExecutionFilterTest {

    public static final String SAMPLE_INVOCATION = "any.package.with.Class.andMethod()";
    private final AntPathMatcher matcher = new AntPathMatcher(".");

    @Test
    void filterInWithEmptyInclusionAndExclusionPatterns() {
        ClassExecutionFilter classExecutionFilter = new ClassExecutionFilter(matcher, emptyList(), emptyList());
        assertThat(classExecutionFilter.filter(SAMPLE_INVOCATION)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "any.**",
            "any.package.**",
            "any.**.andMethod*",
            "any.**.andMethod()",
            "**"
    })
    void filterOutWithMatchingExclusionPattern(String pattern) {
        ClassExecutionFilter classExecutionFilter = new ClassExecutionFilter(matcher, emptyList(), singletonList(pattern));
        assertThat(classExecutionFilter.filter(SAMPLE_INVOCATION)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "any.*",
            "any.package.*",
            "any.**.andMethod",
            "any.**.andMethod(",
            "*"
    })
    void filterInWithNotMatchingExclusionPattern(String pattern) {
        ClassExecutionFilter classExecutionFilter = new ClassExecutionFilter(matcher, emptyList(), singletonList(pattern));
        assertThat(classExecutionFilter.filter(SAMPLE_INVOCATION)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "any.*",
            "any.package.*",
            "any.**.andMethod",
            "any.**.andMethod(",
            "*"
    })
    void filterOutWithNotMatchingInclusionPattern(String pattern) {
        ClassExecutionFilter classExecutionFilter = new ClassExecutionFilter(matcher, singletonList(pattern), emptyList());
        assertThat(classExecutionFilter.filter(SAMPLE_INVOCATION)).isFalse();
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "any.**",
            "any.package.**",
            "any.**.andMethod*",
            "any.**.andMethod()",
            "**"
    })
    void filterOutWithMatchingExclusionAndInclusionPattern(String pattern) {
        ClassExecutionFilter classExecutionFilter = new ClassExecutionFilter(matcher, singletonList(pattern), singletonList(pattern));
        assertThat(classExecutionFilter.filter(SAMPLE_INVOCATION)).isFalse();
    }
}