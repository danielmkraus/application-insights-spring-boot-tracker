package org.sample.service;

import io.github.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking;
import io.github.danielmkraus.applicationinsights.annotation.DisableApplicationInsightsTracking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@ApplicationInsightsTracking
@Service
public class SampleAnnotationService {
    private static final Logger LOG = LoggerFactory.getLogger(SampleService.class);

    public void save(Object object) {
        LOG.info(String.valueOf(object));
    }

    public void saveWithException() {
        LOG.info("Whoops!");
        throw new IllegalStateException();
    }

    @DisableApplicationInsightsTracking
    public void noTraces() {
        LOG.info("Stealth mode ON!");
    }
}
