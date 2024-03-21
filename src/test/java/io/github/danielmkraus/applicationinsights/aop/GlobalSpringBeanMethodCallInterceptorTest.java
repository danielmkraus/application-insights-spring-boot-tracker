package io.github.danielmkraus.applicationinsights.aop;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.RemoteDependencyTelemetry;
import io.github.danielmkraus.applicationinsights.annotation.EnableApplicationInsightsDependencyTracker;
import org.apache.logging.log4j.util.Strings;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {
        SampleController.class,
        SampleService.class,
        SampleRepository.class
})
@DirtiesContext
@EnableApplicationInsightsDependencyTracker
class GlobalSpringBeanMethodCallInterceptorTest {

    @MockBean
    TelemetryClient telemetryClient;

    @MockBean
    ClassExecutionFilter classExecutionFilter;

    @Autowired
    SampleController sampleController;

    @Test
    void allClassesFilteredOut() {
        when(classExecutionFilter.matches(any(Class.class))).thenReturn(Boolean.FALSE);

        sampleController.save("hi!");
        sampleController.save("hi!");
        sampleController.save("hi!");
        sampleController.save("hi!");

        verifyNoInteractions(telemetryClient);
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