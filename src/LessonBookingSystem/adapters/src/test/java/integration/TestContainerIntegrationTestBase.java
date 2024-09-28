package integration;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.EventStoreDBProjectionManagementClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

// This is the base class for all the integration tests using the TestContainer.
// This class contains all the common code shared by all the integration tests using the TestContainer library
public abstract class TestContainerIntegrationTestBase {
    protected static final String CONNECTION_STRING_TEMPLATE = "esdb://admin:changeit@localhost:%s?tls=false&tlsVerifyCert=false";
    protected static final int EVENT_STORE_PORT = 2113;

    protected final Map<String, String> ENV_CONFIGURATION = Map.ofEntries(
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
    protected final GenericContainer<?> eventStoreDbContainer =
            new GenericContainer<>(DockerImageName.parse("eventstore/eventstore:latest"))
                    .withExposedPorts(EVENT_STORE_PORT)
                    .withEnv(ENV_CONFIGURATION);

    @BeforeEach
    public void beforeEach() {
        eventStoreDbContainer.start();
    }

    @AfterEach
    public void afterEach() {
        eventStoreDbContainer.stop();
    }

    protected EventStoreDBProjectionManagementClient getProjectionClient() {
        var port = eventStoreDbContainer.getMappedPort(EVENT_STORE_PORT);
        var connectionString = String.format(CONNECTION_STRING_TEMPLATE, port);
        var settings = EventStoreDBConnectionString.parseOrThrow(connectionString);
        return EventStoreDBProjectionManagementClient.create(settings);
    }

    protected EventStoreDBClient getEventStoreDbClient() {
        var port = eventStoreDbContainer.getMappedPort(EVENT_STORE_PORT);
        var connectionString = String.format(CONNECTION_STRING_TEMPLATE, port);
        var settings = EventStoreDBConnectionString.parseOrThrow(connectionString);
        return EventStoreDBClient.create(settings);
    }
}
