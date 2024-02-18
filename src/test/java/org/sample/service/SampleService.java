package org.sample.service;

import org.sample.repository.SampleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SampleService {

    private static final Logger LOG = LoggerFactory.getLogger(SampleService.class);

    private final SampleRepository repository;

    public SampleService(SampleRepository repository) {
        this.repository = repository;
    }

    public void save(Object object) {
        repository.save(object);
    }

    public void saveWithException() {
        LOG.info("Whoops!");
        repository.save(null);
        throw new IllegalStateException();
    }
}
