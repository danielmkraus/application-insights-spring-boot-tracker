package org.sample.controller;

import org.sample.service.SampleAnnotationService;
import org.springframework.stereotype.Controller;

@Controller
public class SampleAnnotationController {

    private final SampleAnnotationService service;

    public SampleAnnotationController(SampleAnnotationService service) {
        this.service = service;
    }

    public void save(Object object) {
        service.save(object);
    }
}
