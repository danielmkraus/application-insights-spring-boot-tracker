package io.github.danielmkraus.applicationinsights.configuration;

import io.github.danielmkraus.applicationinsights.annotation.EnableApplicationInsightsDependencyTracker;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

public class DependencyTrackerInterceptorRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        Map<String, Object> map =
                importingClassMetadata.getAnnotationAttributes(
                        EnableApplicationInsightsDependencyTracker.class.getName());
        DependencyTrackerBeanDefinitionRegistry
                .getInstance()
                .register(map);
    }
}
