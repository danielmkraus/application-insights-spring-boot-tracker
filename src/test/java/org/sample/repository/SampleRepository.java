package org.sample.repository;

import org.awaitility.Awaitility;
import org.sample.service.SampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.Duration;

import static java.lang.String.valueOf;

@Repository
public class SampleRepository {

    private static final Logger LOG = LoggerFactory.getLogger(SampleService.class);

    public void save(Object object) {
        LOG.info(valueOf(object));
    }
}
