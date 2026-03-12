package br.com.paulohenrique.delivery_orders_api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.net.URI;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfig {
    @Bean
    @ServiceConnection(name = "redis")
    GenericContainer<?> redis() {
        return new GenericContainer<>("redis:7.4-alpine")
                .withEnv("REDIS_PASSWORD", "password")
                .withExposedPorts(6379);
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgres() {
        return new PostgreSQLContainer<>("postgres:18-alpine");
    }

    @Bean
    GenericContainer<?> elasticMq() {
        GenericContainer<?> container = new GenericContainer<>("softwaremill/elasticmq-native:1.6.16")
                .withCopyFileToContainer(MountableFile.forHostPath("elasticmq.conf"), "/opt/elasticmq.conf")
                .withEnv("JAVA_OPTS", "-Dconfig.file=/opt/elasticmq.conf")
                .withExposedPorts(9324);
        container.start();

        return container;
    }

    @Bean
    SqsAsyncClient sqsAsyncClient(GenericContainer<?> elasticMq) {
        return SqsAsyncClient.builder()
                .endpointOverride(URI.create("http://localhost:" + elasticMq.getMappedPort(9324)))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("test", "test")
                        )
                )
                .region(Region.of("sa-east-1"))
                .build();
    }
}
