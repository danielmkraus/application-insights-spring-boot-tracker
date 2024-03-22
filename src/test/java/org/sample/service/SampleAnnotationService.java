package org.sample.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SampleAnnotationService {
    private static final Logger LOG = LoggerFactory.getLogger(SampleService.class);

    public void save(Object object) {
        LOG.info(String.valueOf(object));
    }
}
