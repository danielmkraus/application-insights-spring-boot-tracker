package org.danielmkraus.applicationinsights.configuration;

import com.microsoft.applicationinsights.TelemetryClient;
import org.danielmkraus.applicationinsights.aop.SpringBeanMethodCallInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableConfigurationProperties
@ConditionalOnProperty(name = "spring.application-insights.tracker.enabled", havingValue = "true", matchIfMissing = true)
public class DependencyTrackerInterceptorConfiguration {

    @Bean
    @ConfigurationProperties("spring.application-insights.tracker")
    public DependencyTrackerInterceptorConfigurationProperties dependencyTrackerInterceptorConfigurationProperties() {
        return new DependencyTrackerInterceptorConfigurationProperties();
    }

    @Bean
    public TelemetryClient telemetryClient() {
        return new TelemetryClient();
    }

    @Bean
    public ClassExecutionFilter classFilter(DependencyTrackerInterceptorConfigurationProperties properties) {
        return new ClassExecutionFilter(
                new AntPathMatcher("."),
                properties.getIncludePackages(),
                properties.getExcludePackages());
    }

    @Bean
    public SpringBeanMethodCallInterceptor springBeanMethodCallInterceptor(ClassExecutionFilter classExecutionFilter,
                                                                           TelemetryClient client) {
        return new SpringBeanMethodCallInterceptor(
                client, classExecutionFilter, "InProc");
    }

}
