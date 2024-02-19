package org.danielmkraus.applicationinsights.configuration;

import com.microsoft.applicationinsights.TelemetryClient;
import org.danielmkraus.applicationinsights.aop.AnnotationSpringBeanMethodCallInterceptor;
import org.danielmkraus.applicationinsights.aop.GlobalSpringBeanMethodCallInterceptor;
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

    public static final String DEPENDENCY_TYPE = "InProc";

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
    public AnnotationSpringBeanMethodCallInterceptor annotationSpringBeanMethodCallInterceptor(
            ClassExecutionFilter classExecutionFilter,
            TelemetryClient client) {
        return new AnnotationSpringBeanMethodCallInterceptor(
                tracker(client, classExecutionFilter));
    }

    @Bean
    @ConditionalOnProperty(name = "spring.application-insights.tracker.global-interceptor-enabled", havingValue = "true", matchIfMissing = true)
    public GlobalSpringBeanMethodCallInterceptor globalSpringBeanMethodCallInterceptor(
            ClassExecutionFilter classExecutionFilter,
            TelemetryClient client) {
        return new GlobalSpringBeanMethodCallInterceptor(
                tracker(client, classExecutionFilter));
    }

    public ApplicationInsightsTracker tracker(TelemetryClient telemetryClient, ClassExecutionFilter classExecutionFilter) {
        return new ApplicationInsightsTracker(telemetryClient, DEPENDENCY_TYPE, classExecutionFilter);
    }
}
