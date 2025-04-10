package com.pii.e2e;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

@Testcontainers
public abstract class BaseE2ETest {

    protected static final DockerComposeContainer<?> environment;

    static {
        environment = new DockerComposeContainer<>(new File("docker-compose-e2e.yaml"))
                .withExposedService(
                        "users-db",
                        5432,
                        Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60))
                )
                .withExposedService(
                        "companies-db",
                        5432,
                        Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60))
                )
                .withExposedService(
                        "discovery-service",
                        8761,
                        Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60))
                )
                .withExposedService(
                        "user-service",
                        8081,
                        Wait.forHttp("/actuator/health").withStartupTimeout(Duration.ofSeconds(60))
                )
                .withExposedService(
                        "company-service",
                        8082,
                        Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(60))
                )
                .withLocalCompose(false);
        environment.start();
    }

    protected static String getServiceUrl(String serviceName, int port) {
        return "http://" + environment.getServiceHost(serviceName, port) +
                ":" + environment.getServicePort(serviceName, port);
    }
}
