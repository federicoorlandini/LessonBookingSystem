package integration;

import com.federico.LessonBookingSystem.adapters.out.persistence.EventStoreLessonProjectionsRepository;
import com.federico.LessonBookingSystem.adapters.out.persistence.EventStoreLessonRepository;
import com.federico.LessonBookingSystem.application.usecases.CreateLessonUseCaseImpl;
import com.federico.LessonBookingSystem.application.usecases.GetLessonsOverviewUseCaseImpl;
import jdk.jshell.spi.ExecutionControl;
import Lesson.Lesson;
import Lesson.DuplicatedLessonException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

@Testcontainers(disabledWithoutDocker = true)
public class GetLessonOverviewUseCaseTests extends TestContainerIntegrationTestBase {
    @Test
    public void GetLessonOverviewUseCase_createOneLesson_eventModellingScenario1() throws IOException, ExecutionException, InterruptedException, ExecutionControl.NotImplementedException, DuplicatedLessonException {
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
        final var projectionClient = getProjectionClient();
        final var projectionRepository = new EventStoreLessonProjectionsRepository(projectionClient);

        final var createLessonUseCase = new CreateLessonUseCaseImpl(lessonRepository, projectionRepository);

        final LocalDate tomorrow = LocalDate.now().plusDays(1);
        final LocalTime startTime = LocalTime.of(19, 30);
        final LocalTime endTime = LocalTime.of(20, 30);
        final int maxNumberAttenders = 16;

        projectionRepository.EnsureProjectionExists();
        final var projectionUseCase = new GetLessonsOverviewUseCaseImpl(projectionRepository);

        createLessonUseCase.CreateLesson(tomorrow, startTime, endTime, maxNumberAttenders);

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

    @Test
    public void GetLessonOverviewUseCase_createTwoLessons_eventModellingScenario2() throws IOException, ExecutionException, InterruptedException, ExecutionControl.NotImplementedException, DuplicatedLessonException {
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

        // Arrange
        final var client = getEventStoreDbClient();
        final var lessonRepository = new EventStoreLessonRepository(client);
        final var projectionClient = getProjectionClient();
        final var projectionRepository = new EventStoreLessonProjectionsRepository(projectionClient);

        final var createLessonUseCase = new CreateLessonUseCaseImpl(lessonRepository, projectionRepository);

        // Create first lesson
        final LocalDate tomorrow = LocalDate.now().plusDays(1);
        final LocalTime startTimeLesson1 = LocalTime.of(19, 30);
        final LocalTime endTimeLesson1 = LocalTime.of(20, 30);
        final int maxNumberAttendersLesson1 = 16;
        createLessonUseCase.CreateLesson(tomorrow, startTimeLesson1, endTimeLesson1, maxNumberAttendersLesson1);

        // Create second lesson
        final LocalTime startTimeLesson2 = LocalTime.of(20, 30);
        final LocalTime endTimeLesson2 = LocalTime.of(21, 30);
        final int maxNumberAttendersLesson2 = 16;
        createLessonUseCase.CreateLesson(tomorrow, startTimeLesson2, endTimeLesson2, maxNumberAttendersLesson2);


        projectionRepository.EnsureProjectionExists();
        final var projectionUseCase = new GetLessonsOverviewUseCaseImpl(projectionRepository);

        // Act
        var projection = projectionUseCase.GetLessonsProjection();

        // Assert
        Assertions.assertThat(projection).hasSize(2);
        var firstLessonInProjection = projection.getFirst();
        Assertions.assertThat(firstLessonInProjection.getDate()).isEqualTo(tomorrow);
        Assertions.assertThat(firstLessonInProjection.getStartTime()).isEqualTo(startTimeLesson1);
        Assertions.assertThat(firstLessonInProjection.getEndTime()).isEqualTo(endTimeLesson1);
        Assertions.assertThat(firstLessonInProjection.getMaxNumberAttenders()).isEqualTo(maxNumberAttendersLesson1);
        Assertions.assertThat(firstLessonInProjection.getStatus()).isEqualTo(Lesson.Status.OPEN);
        var secondLessonInProjection = projection.get(1);
        Assertions.assertThat(secondLessonInProjection.getDate()).isEqualTo(tomorrow);
        Assertions.assertThat(secondLessonInProjection.getStartTime()).isEqualTo(startTimeLesson2);
        Assertions.assertThat(secondLessonInProjection.getEndTime()).isEqualTo(endTimeLesson2);
        Assertions.assertThat(secondLessonInProjection.getMaxNumberAttenders()).isEqualTo(maxNumberAttendersLesson2);
        Assertions.assertThat(secondLessonInProjection.getStatus()).isEqualTo(Lesson.Status.OPEN);
    }
}
