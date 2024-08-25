package integration;

import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.EventStoreDBProjectionManagementClient;
import com.federico.LessonBookingSystem.adapters.out.persistence.EventStoreLessonProjectionsRepository;
import com.federico.LessonBookingSystem.application.projections.ports.out.persistence.LessonProjectionsRepository;
import jdk.jshell.spi.ExecutionControl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Testcontainers(disabledWithoutDocker = true)
public class EventStoreLessonProjectionsRepositoryTests {
    private static final String CONNECTION_STRING_TEMPLATE = "esdb://admin:changeit@localhost:%s?tls=false&tlsVerifyCert=false";
    private static final int EVENT_STORE_PORT = 2113;

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
                    .withExposedPorts(EVENT_STORE_PORT)
                    .withEnv(ENV_CONFIGURATION);

    private EventStoreDBProjectionManagementClient projectionClient;

    @BeforeEach
    public void beforeEach() {
        eventStoreDbContainer.start();

        projectionClient = getProjectionClient();
    }

    @AfterEach
    public void afterEach() {
        eventStoreDbContainer.stop();
    }

    @Test
    public void EventStoreLessonProjectionsRepositoryTests_topLevelContainerShouldBeRunning() {
        Assertions.assertThat(eventStoreDbContainer.isRunning()).isTrue();
    }

    @Test
    public void EnsureProjectionExists_shouldCreateTheProjection() throws ExecutionControl.NotImplementedException, ExecutionException, InterruptedException {
        var repository = new EventStoreLessonProjectionsRepository(projectionClient);

        repository.EnsureProjectionExists();

        var projections = projectionClient.list().get();
        Assertions.assertThat(projections).anyMatch(p -> p.getName().equalsIgnoreCase(LessonProjectionsRepository.LESSONS_PROJECTION_NAME));
    }

    private EventStoreDBProjectionManagementClient getProjectionClient() {
        var port = eventStoreDbContainer.getMappedPort(EVENT_STORE_PORT);
        var connectionString = String.format(CONNECTION_STRING_TEMPLATE, port);
        var settings = EventStoreDBConnectionString.parseOrThrow(connectionString);
        return EventStoreDBProjectionManagementClient.create(settings);
    }
}
