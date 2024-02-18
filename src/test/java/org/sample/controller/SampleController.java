package org.sample.controller;


import org.sample.service.SampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

@Controller
public class SampleController {

    private static final Logger LOG = LoggerFactory.getLogger(SampleController.class);

    private final SampleService service;

    public SampleController(SampleService service) {
        this.service = service;
    }

    public void save(Object object) {
        LOG.info("Saving data");
        service.save(object);
    }

    public void saveWithException() {
        LOG.info("Whoops");
        service.saveWithException();
    }
}
