package org.danielmkraus.applicationinsights.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class DependencyTrackerInterceptorConfigurationPropertiesTest {

    private DependencyTrackerInterceptorConfigurationProperties properties;

    @BeforeEach
    void setup(){
        properties = new DependencyTrackerInterceptorConfigurationProperties();
    }

    @Test
    void enabled(){
        properties.setEnabled(true);
        assertThat(properties.isEnabled()).isTrue();

        properties.setEnabled(false);
        assertThat(properties.isEnabled()).isFalse();
    }

    @Test
    void includePackages(){
        properties.setIncludePackages(null);
        assertThat(properties.getIncludePackages()).isNull();

        properties.setIncludePackages(Collections.singletonList("a"));
        assertThat(properties.getIncludePackages()).containsExactly("a");
    }

    @Test
    void excludePackages(){
        properties.setExcludePackages(null);
        assertThat(properties.getExcludePackages()).isNull();

        properties.setExcludePackages(Collections.singletonList("a"));
        assertThat(properties.getExcludePackages()).containsExactly("a");
    }
}