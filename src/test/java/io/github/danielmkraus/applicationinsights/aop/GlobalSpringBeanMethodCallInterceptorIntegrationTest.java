package io.github.danielmkraus.applicationinsights.aop;

import com.microsoft.applicationinsights.TelemetryClient;
import io.github.danielmkraus.applicationinsights.annotation.EnableApplicationInsightsDependencyTracer;
import org.junit.jupiter.api.Test;
import org.sample.controller.SampleController;
import org.sample.repository.SampleRepository;
import org.sample.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static io.github.danielmkraus.applicationinsights.aop.GlobalSpringBeanMethodCallInterceptorTest.methodExecution;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {
        SampleController.class,
        SampleService.class,
        SampleRepository.class
})
@DirtiesContext
@TestPropertySource("classpath:application.yml")
@EnableApplicationInsightsDependencyTracer
class GlobalSpringBeanMethodCallInterceptorIntegrationTest {

    @Autowired
    SampleController controller;

    @SpyBean
    TelemetryClient telemetryClient;

    @Test
    void filtersOutClassInExclusionFilter() {
        controller.save("hi");

        verify(telemetryClient)
                .trackDependency(argThat(methodExecution("public void org.sample.repository.SampleRepository.save(java.lang.Object)")));
        verify(telemetryClient)
                .trackDependency(argThat(methodExecution("public void org.sample.controller.SampleController.save(java.lang.Object)")));

        //as it was on exclusion filters, should not be tracked
        verify(telemetryClient, times(0))
                .trackDependency(argThat(methodExecution("public void org.sample.service.SampleService.save(java.lang.Object)")));
    }

}