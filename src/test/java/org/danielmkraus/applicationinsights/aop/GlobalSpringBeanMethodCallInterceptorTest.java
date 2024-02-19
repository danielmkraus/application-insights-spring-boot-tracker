package org.danielmkraus.applicationinsights.aop;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.RemoteDependencyTelemetry;
import org.apache.logging.log4j.util.Strings;
import org.danielmkraus.applicationinsights.configuration.ClassExecutionFilter;
import org.danielmkraus.applicationinsights.configuration.DependencyTrackerInterceptorConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.sample.controller.SampleController;
import org.sample.repository.SampleRepository;
import org.sample.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = {
        DependencyTrackerInterceptorConfiguration.class,
        SampleController.class,
        SampleService.class,
        SampleRepository.class
})
@DirtiesContext
class GlobalSpringBeanMethodCallInterceptorTest {

    @MockBean
    TelemetryClient telemetryClient;

    @MockBean
    ClassExecutionFilter classExecutionFilter;

    @Autowired
    SampleController sampleController;

    @Test
    void allClassesFilteredOut() {
        when(classExecutionFilter.filter(any(String.class))).thenReturn(Boolean.FALSE);

        sampleController.save("hi!");

        verify(classExecutionFilter).filter(SampleController.class.getCanonicalName());
        verify(classExecutionFilter).filter(SampleService.class.getCanonicalName());
        verify(classExecutionFilter).filter(SampleRepository.class.getCanonicalName());

        verifyNoInteractions(telemetryClient);
    }

    @Test
    void allClassesIncluded() {
        when(classExecutionFilter.filter(any(String.class))).thenReturn(Boolean.TRUE);

        sampleController.save("hi!");

        verify(classExecutionFilter).filter(SampleController.class.getCanonicalName());
        verify(classExecutionFilter).filter(SampleService.class.getCanonicalName());
        verify(classExecutionFilter).filter(SampleRepository.class.getCanonicalName());

        verify(telemetryClient).trackDependency(argThat(methodExecution("public void org.sample.repository.SampleRepository.save(java.lang.Object)")));
        verify(telemetryClient).trackDependency(argThat(methodExecution("public void org.sample.service.SampleService.save(java.lang.Object)")));
        verify(telemetryClient).trackDependency(argThat(methodExecution("public void org.sample.controller.SampleController.save(java.lang.Object)")));

    }

    @Test
    void allClassesIncludedWithException() {
        when(classExecutionFilter.filter(any(String.class))).thenReturn(Boolean.TRUE);

        assertThatIllegalStateException().isThrownBy(sampleController::saveWithException);

        verify(classExecutionFilter).filter(SampleRepository.class.getCanonicalName());
        verify(classExecutionFilter).filter(SampleService.class.getCanonicalName());
        verify(classExecutionFilter).filter(SampleController.class.getCanonicalName());

        verify(telemetryClient).trackDependency(argThat(methodExecution("public void org.sample.repository.SampleRepository.save(java.lang.Object)")));
        verify(telemetryClient).trackDependency(argThat(failedMethodExecution("public void org.sample.service.SampleService.saveWithException()")));
        verify(telemetryClient).trackDependency(argThat(failedMethodExecution("public void org.sample.controller.SampleController.saveWithException()")));

        verifyNoMoreInteractions(classExecutionFilter, telemetryClient);
    }

    public static ArgumentMatcher<RemoteDependencyTelemetry> failedMethodExecution(String name) {
        return value -> !value.getSuccess()
                && Strings.isNotBlank(value.getCommandName())
                && value.getCommandName().equals(name);
    }

    public static ArgumentMatcher<RemoteDependencyTelemetry> methodExecution(String name) {
        return value -> value.getSuccess()
                && Strings.isNotBlank(value.getCommandName())
                && value.getCommandName().equals(name);
    }

}