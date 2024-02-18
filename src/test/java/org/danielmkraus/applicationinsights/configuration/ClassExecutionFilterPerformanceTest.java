package org.danielmkraus.applicationinsights.configuration;


import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.awaitility.Awaitility.await;

class ClassExecutionFilterPerformanceTest {

    @Test
    void shouldPerformMillionFilterOperationsInLessThanOneSecond() {
        ClassExecutionFilter filter = new ClassExecutionFilter(new AntPathMatcher("."),
                asList("org.springframework..*", "org.danielkraus.**"),
                singletonList("org.springframework.boot.*"));

        IntStream stream = IntStream.range(1, 1_000_000);

        await().atMost(1, TimeUnit.SECONDS)
                .untilAsserted(() -> stream.sequential()
                        .forEach(value -> filter.filter(String.valueOf(value))));

    }
}
