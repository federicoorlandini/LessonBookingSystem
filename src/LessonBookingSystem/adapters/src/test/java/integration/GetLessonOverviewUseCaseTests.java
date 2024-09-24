package integration;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import com.eventstore.dbclient.EventStoreDBProjectionManagementClient;
import com.federico.LessonBookingSystem.adapters.out.persistence.EventStoreDbClientConfiguration;
import com.federico.LessonBookingSystem.adapters.out.persistence.EventStoreLessonProjectionsRepository;
import com.federico.LessonBookingSystem.adapters.out.persistence.EventStoreLessonRepository;
import com.federico.LessonBookingSystem.application.usecases.CreateLessonUseCaseImpl;
import com.federico.LessonBookingSystem.application.usecases.GetLessonsOverviewUseCaseImpl;
import jdk.jshell.spi.ExecutionControl;
import model.Lesson;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Testcontainers(disabledWithoutDocker = true)
public class GetLessonOverviewUseCaseTests {
    // TODO - Refactor because it is duplicated
    private static final String CONNECTION_STRING_TEMPLATE = "esdb://admin:changeit@localhost:%s?tls=false&tlsVerifyCert=false";
    private static final int EVENT_STORE_PORT = 2113;

    // TODO - Refactor because it is duplicated
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
    // TODO - Refactor because it is duplicated
    private final GenericContainer<?> eventStoreDbContainer =
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

    @Test
    public void GetLessonOverviewUseCase_createOneLesson_eventModellingScenario1() throws IOException, ExecutionException, InterruptedException, ExecutionControl.NotImplementedException {
        // Given I send an event CreateLesson with
        //  - Date: tomorrow
        //  - StartTime: 19:30
        //  - EndTime: 20:30
        //  - MaxNumberAttenders: 16
        // When I query the GetLessonOverviewUseCase
        // Then I should see the correct projection with only one item with the following properties:
        //  - Date: tomorrow
        //  - StartTime: 19:30
        //  - EndTime: 20:30
        //  - MaxNumberAttenders: 16
        //  - Num. Attenders: 0
        //  - Status: Open

        // Arrange
        final var client = getEventStoreDbClient();
        final var lessonRepository = new EventStoreLessonRepository(client);
        final var createLessonUseCase = new CreateLessonUseCaseImpl(lessonRepository);

        final LocalDate tomorrow = LocalDate.now().plusDays(1);
        final LocalTime startTime = LocalTime.of(19, 30);
        final LocalTime endTime = LocalTime.of(20, 30);
        final int maxNumberAttenders = 16;

        createLessonUseCase.CreateLesson(tomorrow, startTime, endTime, maxNumberAttenders);

        final var projectionClient = getProjectionClient();
        final var projectionRepository = new EventStoreLessonProjectionsRepository(projectionClient);
        projectionRepository.EnsureProjectionExists();
        final var projectionUseCase = new GetLessonsOverviewUseCaseImpl(projectionRepository);

        // Act
        var projection = projectionUseCase.GetLessonsProjection();

        // Assert
        Assertions.assertThat(projection).hasSize(1);
        var lessonInProjection = projection.getFirst();
        Assertions.assertThat(lessonInProjection.getDate()).isEqualTo(tomorrow);
        Assertions.assertThat(lessonInProjection.getStartTime()).isEqualTo(startTime);
        Assertions.assertThat(lessonInProjection.getEndTime()).isEqualTo(endTime);
        Assertions.assertThat(lessonInProjection.getMaxNumberAttenders()).isEqualTo(maxNumberAttenders);
        Assertions.assertThat(lessonInProjection.getStatus()).isEqualTo(Lesson.Status.OPEN);
    }

    public void GetLessonOverviewUseCase_createTwoLessons_eventModellingScenario2() {
        // Given I send an event CreateLesson with
        //  - Date: tomorrow
        //  - StartTime: 19:30
        //  - EndTime: 20:30
        //  - MaxNumberAttenders: 16
        // And I send another event CreateLesson with
        //  - Date: tomorrow
        //  - StartTime: 20:30
        //  - EndTime: 21:30
        //  - MaxNumberAttenders: 12
        // When I query the GetLessonOverviewUseCase
        // Then I should see the correct projection
        //  + Date     + StartTime + EndTime + Max. Attenders + Num. Attenders + Status +
        //  | tomorrow | 19:30     | 20:30   | 16             | 0              | Open   |
        //  | tomorrow | 20:30     | 21:30   | 12             | 0              | Open   |
    }

    // TODO - Duplicated code (see EventStoreLessonProjectionsRepositoryTests)
    private EventStoreDBProjectionManagementClient getProjectionClient() {
        var port = eventStoreDbContainer.getMappedPort(EVENT_STORE_PORT);
        var connectionString = String.format(CONNECTION_STRING_TEMPLATE, port);
        var settings = EventStoreDBConnectionString.parseOrThrow(connectionString);
        return EventStoreDBProjectionManagementClient.create(settings);
    }

    // TODO - Duplicated code (see EventStoreLessonProjectionsRepositoryTests)
    private EventStoreDBClient getEventStoreDbClient() {
        var port = eventStoreDbContainer.getMappedPort(EVENT_STORE_PORT);
        var connectionString = String.format(CONNECTION_STRING_TEMPLATE, port);
        var settings = EventStoreDBConnectionString.parseOrThrow(connectionString);
        return EventStoreDBClient.create(settings);
    }
}
