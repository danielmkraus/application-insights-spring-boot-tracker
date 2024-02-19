package io.github.danielmkraus.applicationinsights.configuration;

import io.github.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking;
import io.github.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking;

import java.util.List;

/**
 * Application Insights Spring bean method execution dependency Tracker configuration properties.
 *
 * @see DependencyTrackerInterceptorConfiguration
 */
public class DependencyTrackerInterceptorConfigurationProperties {

    /**
     * Specifies a list of packages using Ant path expression notation that will be included in the bean interceptor.
     * It's important to note that if a package matches patterns specified in both the include and exclude packages,
     * the telemetry for intercepted bean calls within that package will not be included.
     *
     * @see org.springframework.util.AntPathMatcher
     */
    private List<String> includePackages;

    /**
     * Specifies a list of packages using Ant path expression notation that will be excluded in the bean interceptor.
     * It's important to note that if a package matches patterns specified in both the include and exclude packages,
     * the telemetry for intercepted bean calls within that package will not be included.
     *
     * @see org.springframework.util.AntPathMatcher
     */
    private List<String> excludePackages;

    /**
     * Controls the activation of the Application Insights Spring bean calls tracker.
     */
    private boolean enabled;

    public boolean isGlobalInterceptorEnabled() {
        return globalInterceptorEnabled;
    }

    public void setGlobalInterceptorEnabled(boolean globalInterceptorEnabled) {
        this.globalInterceptorEnabled = globalInterceptorEnabled;
    }

    /**
     * Controls the activation of the global interceptor for Application Insights Spring bean calls tracker.
     * If this configuration is not active, only the beans annotated with
     * {@link ApplicationInsightsTracking} will be tracked
     *
     * @see ApplicationInsightsTracking
     * @see DisableApplicationInsightsTracking
     */
    private boolean globalInterceptorEnabled;

    public List<String> getIncludePackages() {
        return includePackages;
    }

    public void setIncludePackages(List<String> includePackages) {
        this.includePackages = includePackages;
    }

    public List<String> getExcludePackages() {
        return excludePackages;
    }

    public void setExcludePackages(List<String> excludePackages) {
        this.excludePackages = excludePackages;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
