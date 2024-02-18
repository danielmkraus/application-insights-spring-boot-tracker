[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=application-insights-spring-boot-tracker)](https://sonarcloud.io/summary/new_code?id=application-insights-spring-boot-tracker)
[![Maven Package](https://github.com/danielmkraus/application-insights-spring-boot-tracker/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/danielmkraus/application-insights-spring-boot-tracker/actions/workflows/maven-publish.yml)

# Microsoft Azure Application Insights Spring bean calls tracker

## Motivation

In both performance optimization and troubleshooting scenarios, the more comprehensive the data collected and available 
for analysis, the better. This enables a deeper understanding of the application, facilitates pinpointing problematic 
areas within the software, and provides insights into the root causes of issues. It's important to note that not all 
external connections, such as LDAP connectivity, are tracked by [Microsoft Azure Application Insights](https://learn.microsoft.com/en-us/azure/azure-monitor/app/app-insights-overview). 
Therefore, there's a necessity to gather additional runtime execution data for ongoing calls.

## Objective

- Track Spring Bean calls in [Microsoft Azure Application Insights](https://learn.microsoft.com/en-us/azure/azure-monitor/app/app-insights-overview) 
as Dependencies

## How it works

The system integrates a  [Spring AOP](https://docs.spring.io/spring-framework/reference/core/aop.html) aspect to monitor 
Spring calls, recording their execution time. These details are then transmitted to Azure Application Insights utilizing 
their SDK for further analysis and monitoring.

## Usage

If you have already configured Azure Application Insights in your Spring Boot Application, follow these steps:

1. Add the project dependency to your project:
   - Maven:
    ```xml
    <dependency>
        <groupId>org.danielkraus</groupId>
        <artifactId>application-insights-spring-boot-tracker</artifactId>
        <version>1.0.0</version>
    </dependency>
    ```
   - Gradle: 
   ```groovy
   implementation 'org.danielkraus:application-insights-spring-boot-tracker:1.0.0'
   ```

2. Add the Auto Configuration annotation `@org.danielmkraus.applicationinsights.configuration.EnableApplicationInsightsDependencyTracer` 
to your spring configuration.
3. Check the transaction details in Azure Application Insights, you should be able to see the Spring bean calls if this
library is correctly configured.
    - Before
    - After

## Configuration properties

### Properties

Following below all available configuration properties.
It's important to note that if a package matches patterns specified in both the include and exclude packages, the 
telemetry for intercepted bean calls within that package will not be included.

#### spring.application-insights.tracker.enabled

Controls the activation of the Application Insights Spring bean calls tracker.
Enabled by default

#### spring.application-insights.tracker.exclude-packages

Specifies a list of packages using [Ant path expression notation](https://docs.spring.io/spring-framework/docs/3.2.0.RELEASE_to_3.2.1.RELEASE/Spring%20Framework%203.2.1.RELEASE/org/springframework/util/AntPathMatcher.html) 
that will be included in the bean interceptor. If no value is specified, all packages will be included. No value 
specified by default

#### spring.application-insights.tracker.exclude-packages

Specifies a list of packages using [Ant path expression notation](https://docs.spring.io/spring-framework/docs/3.2.0.RELEASE_to_3.2.1.RELEASE/Spring%20Framework%203.2.1.RELEASE/org/springframework/util/AntPathMatcher.html) 
that will be excluded in the bean interceptor. If no value is specified, no exclusion will be applied. No value 
specified by default

### Examples

#### yml file

```yaml
spring:
  application-insights:
    tracker:
      enabled: true
      exclude-packages:
        - org.sample.service.**
        - org.springframework.**
      include-packages:
        - org.sample.**
        - com.mycompany.**
```

#### properties file

```properties
spring.application-insights.tracker.enabled=true
spring.application-insights.tracker.exclude-packages[0]=org.sample.service.**
spring.application-insights.tracker.exclude-packages[1]=org.springframework.**
spring.application-insights.tracker.include-packages[0]=org.sample.**
spring.application-insights.tracker.include-packages[1]=com.mycompany.**
```

