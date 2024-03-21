package io.github.danielmkraus.applicationinsights.configuration;

import com.microsoft.applicationinsights.TelemetryClient;
import io.github.danielmkraus.applicationinsights.aop.ApplicationInsightsTrackerAutoProxyCreator;
import io.github.danielmkraus.applicationinsights.aop.ClassExecutionFilter;
import io.github.danielmkraus.applicationinsights.aop.GlobalSpringBeanMethodCallInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(name = "spring.application-insights.tracker.enabled", havingValue = "true", matchIfMissing = true)
public class DependencyTrackerInterceptorConfiguration {

    public static final String DEPENDENCY_TYPE = "InProc";

    @Bean
    public TelemetryClient telemetryClient() {
        return new TelemetryClient();
    }

    @Bean
    @ConfigurationProperties("spring.application-insights.tracker")
    public DependencyTrackerInterceptorConfigurationProperties dependencyTrackerInterceptorConfigurationProperties() {
        return new DependencyTrackerInterceptorConfigurationProperties();
    }

    @Bean
    public ClassExecutionFilter classFilter(DependencyTrackerInterceptorConfigurationProperties properties) {
        return new ClassExecutionFilter(
                new AntPathMatcher("."),
                properties.getIncludePackages(),
                properties.getExcludePackages());
    }

    @Bean
    public GlobalSpringBeanMethodCallInterceptor globalApplicationInsightsSpringBeanMethodCallInterceptor(
            TelemetryClient client) {
        return new GlobalSpringBeanMethodCallInterceptor(
                tracker(client));
    }

    public ApplicationInsightsTracker tracker(TelemetryClient telemetryClient) {
        return new ApplicationInsightsTracker(telemetryClient, DEPENDENCY_TYPE);
    }

    @Bean
    public ApplicationInsightsTrackerAutoProxyCreator autoProxyCreator(ClassExecutionFilter classExecutionFilter) {
        ApplicationInsightsTrackerAutoProxyCreator applicationInsightsTrackerAutoProxyCreator = new ApplicationInsightsTrackerAutoProxyCreator(classExecutionFilter);

        DependencyTrackerBeanDefinitionRegistry registry = DependencyTrackerBeanDefinitionRegistry.getInstance();
        applicationInsightsTrackerAutoProxyCreator.setProxyTargetClass(registry.isProxyTargetClass());
        applicationInsightsTrackerAutoProxyCreator.setExposeProxy(registry.isExposeProxy());
        applicationInsightsTrackerAutoProxyCreator.setInterceptorNames("globalApplicationInsightsSpringBeanMethodCallInterceptor");
        return applicationInsightsTrackerAutoProxyCreator;
    }
}
