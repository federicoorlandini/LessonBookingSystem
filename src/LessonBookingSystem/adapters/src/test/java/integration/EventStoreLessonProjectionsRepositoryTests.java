package integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@Testcontainers(disabledWithoutDocker = true)
public class EventStoreLessonProjectionsRepositoryTests {
    private final Map<String, String> ENV_CONFIGURATION = Map.ofEntries(
            Map.entry("EVENTSTORE_ALLOWUNKNOWNOPTIONS", "true"),
            Map.entry("EVENTSTORE_INSECURE", "true"),
            Map.entry("EVENTSTORE_CLUSTER_SIZE", "1"),
            Map.entry("EVENTSTORE_RUN_PROJECTIONS", "All"),
            Map.entry("EVENTSTORE_START_STANDARD_PROJECTIONS", "true"),
            Map.entry("EVENTSTORE_EXT_TCP_PORT", "1113"),
            Map.entry("EVENTSTORE_EXT_HTTP_PORT", "2113"),
            Map.entry("EVENTSTORE_ENABLE_ATOM_PUB_OVER_HTTP", "true"),
            Map.entry("EVENTSTORE_ENABLE_EXTERNAL_TCP", "true")
    );

    @Container
    private final GenericContainer<?> eventStoreDbContainer =
            new GenericContainer<>(DockerImageName.parse("eventstore/eventstore:latest"))
                    .withExposedPorts(2113)
                    .withEnv(ENV_CONFIGURATION);

    @BeforeEach
    public void beforeEach() {
        eventStoreDbContainer.start();
    }

    @AfterEach
    public void afterEach() {
        eventStoreDbContainer.stop();
    }

    @Test
    public void EventStoreLessonProjectionsRepositoryTests_topLevelContainerShouldBeRunning() {
        Assertions.assertThat(eventStoreDbContainer.isRunning()).isTrue();
    }
}
