package io.github.danielmkraus.applicationinsights.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DependencyTrackerBeanDefinitionRegistryTest {

    private static final DependencyTrackerBeanDefinitionRegistry REGISTRY =
            DependencyTrackerBeanDefinitionRegistry.getInstance();

    @BeforeEach
    void setup() {
        REGISTRY.reset();
    }

    @Test
    void parsesEmptyMap() {
        REGISTRY.register(Collections.emptyMap());

        assertThat(REGISTRY.isExposeProxy())
                .isFalse();
        assertThat(REGISTRY.isProxyTargetClass())
                .isFalse();
    }

    @ParameterizedTest
    @MethodSource("testArguments")
    void parsesBooleanTrue(String attribute, Supplier<Boolean> methodCall) {
        Map<String, Object> values = new HashMap<>();
        values.put(attribute, true);
        REGISTRY.register(values);

        assertThat(methodCall.get())
                .isTrue();
    }

    @ParameterizedTest
    @MethodSource("testArguments")
    void parsesBooleanFalse(String attribute, Supplier<Boolean> methodCall) {
        Map<String, Object> values = new HashMap<>();
        values.put(attribute, false);
        REGISTRY.register(values);

        assertThat(methodCall.get())
                .isFalse();
    }

    @ParameterizedTest
    @MethodSource("testArguments")
    void falseForInvalidString(String attribute, Supplier<Boolean> methodCall) {
        Map<String, Object> values = new HashMap<>();
        values.put(attribute, "invalid");
        REGISTRY.register(values);

        assertThat(methodCall.get())
                .isFalse();
    }

    @ParameterizedTest
    @MethodSource("testArguments")
    void trueForStringTrue(String attribute, Supplier<Boolean> methodCall) {
        Map<String, Object> values = new HashMap<>();
        values.put(attribute, "true");
        REGISTRY.register(values);

        assertThat(methodCall.get())
                .isTrue();
    }

    @ParameterizedTest
    @MethodSource("testArguments")
    void onceTrueAlwaysTrue(String attribute, Supplier<Boolean> methodCall) {
        Map<String, Object> values = new HashMap<>();
        values.put(attribute, "true");
        REGISTRY.register(values);
        values.put(attribute, "false");
        REGISTRY.register(values);
        values.put(attribute, "false");
        REGISTRY.register(values);

        assertThat(methodCall.get())
                .isTrue();
    }

    public static List<Arguments> testArguments() {
        return Arrays.asList(
                arguments("proxyTargetClass", (Supplier<Boolean>) REGISTRY::isProxyTargetClass),
                arguments("exposeProxy", (Supplier<Boolean>) REGISTRY::isExposeProxy)
        );
    }
}