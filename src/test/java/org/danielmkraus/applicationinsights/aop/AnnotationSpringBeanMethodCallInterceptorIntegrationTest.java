package org.danielmkraus.applicationinsights.aop;

import com.microsoft.applicationinsights.TelemetryClient;
import org.danielmkraus.applicationinsights.annotation.EnableApplicationInsightsDependencyTracer;
import org.junit.jupiter.api.Test;
import org.sample.controller.SampleAnnotationController;
import org.sample.service.SampleAnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.danielmkraus.applicationinsights.aop.GlobalSpringBeanMethodCallInterceptorTest.failedMethodExecution;
import static org.danielmkraus.applicationinsights.aop.GlobalSpringBeanMethodCallInterceptorTest.methodExecution;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {
        SampleAnnotationController.class,
        SampleAnnotationService.class
})
@DirtiesContext
@TestPropertySource("classpath:application.yml")
@EnableApplicationInsightsDependencyTracer
class AnnotationSpringBeanMethodCallInterceptorIntegrationTest {

    @Autowired
    SampleAnnotationController controller;

    @SpyBean
    TelemetryClient telemetryClient;

    @Test
    void executesAndRecords() {
        controller.save("hi");
        verify(telemetryClient)
                .trackDependency(argThat(methodExecution("public void org.sample.controller.SampleAnnotationController.save(java.lang.Object)")));
        verify(telemetryClient)
                .trackDependency(argThat(methodExecution("public void org.sample.service.SampleAnnotationService.save(java.lang.Object)")));
    }


    @Test
    void recordsWithExceptionThrown() {

        assertThatIllegalStateException().isThrownBy(controller::saveWithException);

        verify(telemetryClient)
                .trackDependency(argThat(failedMethodExecution("public void org.sample.controller.SampleAnnotationController.saveWithException()")));
        verify(telemetryClient)
                .trackDependency(argThat(failedMethodExecution("public void org.sample.service.SampleAnnotationService.saveWithException()")));
    }

    @Test
    void skipMethodsWithDisabledAnnotation() {
        controller.noTraces();
        verify(telemetryClient)
                .trackDependency(argThat(methodExecution("public void org.sample.controller.SampleAnnotationController.noTraces()")));
        verify(telemetryClient, times(0))
                .trackDependency(argThat(methodExecution("public void org.sample.service.SampleAnnotationService.noTraces()")));
    }

}