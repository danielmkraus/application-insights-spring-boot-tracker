package org.sample.controller;

import io.github.danielmkraus.applicationinsights.annotation.ApplicationInsightsTracking;
import org.sample.service.SampleAnnotationService;
import org.springframework.stereotype.Controller;

@Controller
@ApplicationInsightsTracking
public class SampleAnnotationController {

    private final SampleAnnotationService service;

    public SampleAnnotationController(SampleAnnotationService service) {
        this.service = service;
    }

    public void save(Object object) {
        service.save(object);
    }

    public void saveWithException() {
        service.saveWithException();
    }

    public void noTraces() {
        service.noTraces();
    }
}
