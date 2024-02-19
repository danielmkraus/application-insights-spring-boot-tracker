package io.github.danielmkraus.applicationinsights.aop;

import com.microsoft.applicationinsights.TelemetryClient;
import io.github.danielmkraus.applicationinsights.annotation.EnableApplicationInsightsDependencyTracer;
import org.junit.jupiter.api.Test;
import org.sample.controller.SampleController;
import org.sample.repository.SampleRepository;
import org.sample.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest(classes = {
        SampleController.class,
        SampleService.class,
        SampleRepository.class
})
@DirtiesContext
@TestPropertySource("classpath:application.yml")
@EnableApplicationInsightsDependencyTracer
class GlobalSpringBeanMethodCallInterceptorDisabledIntegrationTest {

    @Autowired
    SampleController controller;

    @MockBean
    TelemetryClient telemetryClient;

    @Test
    void filtersOutClassInExclusionFilter() {
        controller.save("hi");

        verifyNoInteractions(telemetryClient);
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.application-insights.tracker.global-interceptor-enabled", () -> "false");
    }

}