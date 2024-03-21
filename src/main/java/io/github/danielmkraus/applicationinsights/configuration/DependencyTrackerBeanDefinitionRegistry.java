package io.github.danielmkraus.applicationinsights.configuration;

import java.util.Map;

public class DependencyTrackerBeanDefinitionRegistry {

    private static final DependencyTrackerBeanDefinitionRegistry INSTANCE = new DependencyTrackerBeanDefinitionRegistry();

    public static DependencyTrackerBeanDefinitionRegistry getInstance() {
        return INSTANCE;
    }

    private static boolean getBoolean(String key, Map<String, Object> configurations) {
        return Boolean.parseBoolean(
                configurations.getOrDefault(key, false)
                        .toString());
    }

    private DependencyTrackerBeanDefinitionRegistry() {
    }

    private boolean exposeProxy;
    private boolean proxyTargetClass;

    public void register(Map<String, Object> configurations) {
        exposeProxy = exposeProxy || getBoolean("exposeProxy", configurations);
        proxyTargetClass = proxyTargetClass || getBoolean("proxyTargetClass", configurations);
    }

    public boolean isExposeProxy() {
        return exposeProxy;
    }

    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void reset() {
        exposeProxy = false;
        proxyTargetClass = false;
    }
}
