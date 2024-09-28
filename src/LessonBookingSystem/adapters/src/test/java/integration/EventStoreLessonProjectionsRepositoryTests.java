package integration;

import com.eventstore.dbclient.EventStoreDBProjectionManagementClient;
import com.federico.LessonBookingSystem.adapters.out.persistence.EventStoreLessonProjectionsRepository;
import com.federico.LessonBookingSystem.application.projections.ports.out.persistence.LessonProjectionsRepository;
import jdk.jshell.spi.ExecutionControl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.ExecutionException;

@Testcontainers(disabledWithoutDocker = true)
public class EventStoreLessonProjectionsRepositoryTests extends TestContainerIntegrationTestBase {
    private EventStoreDBProjectionManagementClient projectionClient;

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
}
