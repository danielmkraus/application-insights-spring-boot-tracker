package io.github.danielmkraus.applicationinsights.aop;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.lang.Nullable;

/**
 * Bean auto proxy creator utility to map all the spring bean calls listed and send their execution status and time to
 * Application insights
 */
public class ApplicationInsightsTrackerAutoProxyCreator extends AbstractAutoProxyCreator {

    private final ClassExecutionFilter classExecutionFilter;

    public ApplicationInsightsTrackerAutoProxyCreator(ClassExecutionFilter classExecutionFilter) {
        this.classExecutionFilter = classExecutionFilter;
        setProxyTargetClass(true);
    }

    /**
     * Delegate to {@link AbstractAutoProxyCreator#getCustomTargetSource(Class, String)}
     * if the bean name matches one of the names in the configured list of supported
     * names, returning {@code null} otherwise.
     *
     * @since 5.3
     */
    @Override
    protected TargetSource getCustomTargetSource(Class<?> beanClass, String beanName) {
        return (isSupportedBeanName(beanClass) ?
                super.getCustomTargetSource(beanClass, beanName) : null);
    }

    /**
     * Identify as a bean to proxy if the bean name matches one of the names in
     * the configured list of supported names.
     */
    @Override
    @Nullable
    protected Object[] getAdvicesAndAdvisorsForBean(
            Class<?> beanClass, String beanName, @Nullable TargetSource targetSource) {
        return (isSupportedBeanName(beanClass) ?
                PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS : DO_NOT_PROXY);
    }

    /**
     * Determine if the class name for the given bean class matches one of the names in the configured list of supported
     * names and it is not excluded by the exclusion list.
     *
     * @param beanClass the class of the bean to advise
     * @return {@code true} if the given bean name is supported
     */
    private boolean isSupportedBeanName(Class<?> beanClass) {
        return classExecutionFilter.matches(beanClass);
    }

}
