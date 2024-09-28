import Lesson.LessonCommand;
import Lesson.LessonEvent;
import Lesson.Lesson;
import Lesson.DuplicatedLessonException;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.io.InvalidClassException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.List;

public class LessonTests {
    private final LocalDate DATE_IN_THE_FUTURE = LocalDate.now().plusMonths(1);
    private final LocalDate DATE_IN_THE_PAST = LocalDate.now().minusMonths(1);
    private final LocalTime START_TIME = LocalTime.of(10, 0);
    private final LocalTime END_TIME = LocalTime.of(11, 0);
    private final List<Lesson> EMPTY_LESSONS_LIST = List.of();

    @Test
    public void lesson_whenCreatingALesson_theAggregateSetupShouldBeCorrect()
            throws InvalidClassException, DuplicatedLessonException {
        // Arrange
        var lesson = new Lesson();
        var command = new LessonCommand.CreateLessonCommand(
                UUID.randomUUID(),
                DATE_IN_THE_FUTURE,
                START_TIME,
                END_TIME,
                10,
                EMPTY_LESSONS_LIST);

        // Act
        lesson.handle(command);

        // Assert
        assertThat(lesson.getId()).isEqualByComparingTo(command.lessonId());
        assertThat(lesson.getDate()).isEqualTo(command.date());
        assertThat(lesson.getStartTime()).isEqualToIgnoringNanos(command.startTime());
        assertThat(lesson.getEndTime()).isEqualToIgnoringNanos(command.endTime());
        assertThat(lesson.getMaxNumberAttenders()).isEqualTo(command.maxNumberAttenders());
    }

    @Test
    public void lesson_whenCreatingALesson_shouldGenerateACorrectLessonCreatedEvent()
            throws InvalidClassException, DuplicatedLessonException {
        // Arrange
        var lesson = new Lesson();
        var command = new LessonCommand.CreateLessonCommand(
                UUID.randomUUID(),
                DATE_IN_THE_FUTURE,
                START_TIME,
                END_TIME,
                10,
                EMPTY_LESSONS_LIST);

        // Act
        var events = lesson.handle(command);

        // Assert
        assertThat(events).hasSize(1);
        var event = events.getFirst();
        assertThat(event).isInstanceOf(LessonEvent.LessonCreated.class);
        assertThat(event.eventId()).isNotNull();
        assertThat(event.lessonId()).isEqualByComparingTo(command.lessonId());
        var createdEvent = (LessonEvent.LessonCreated)event;
        assertThat(lesson.getDate()).isEqualTo(command.date());
        assertThat(lesson.getStartTime()).isEqualToIgnoringNanos(command.startTime());
        assertThat(lesson.getEndTime()).isEqualToIgnoringNanos(command.endTime());
        assertThat(createdEvent.maxNumberAttenders()).isEqualTo(command.maxNumberAttenders());
    }

    @Test
    public void lesson_whenCreatingALesson_andTheDateIsInThePast_thenShouldThrowAnUnsupportedOperationException() {
        // Arrange
        var lesson = new Lesson();
        var command = new LessonCommand.CreateLessonCommand(
                UUID.randomUUID(),
                DATE_IN_THE_PAST,
                START_TIME,
                END_TIME,
                10,
                EMPTY_LESSONS_LIST);

        // Act and assert
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> lesson.handle(command));
    }

    @Test
    public void lesson_whenCreatingALesson_andTheMaxNumberOfAttendantsIsZero_thenShouldThrowAnUnsupportedOperationException() {
        // Arrange
        var lesson = new Lesson();
        var command = new LessonCommand.CreateLessonCommand(
                UUID.randomUUID(),
                DATE_IN_THE_FUTURE,
                START_TIME,
                END_TIME,
                0,
                EMPTY_LESSONS_LIST);

        // Act and assert
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> lesson.handle(command));
    }

    @Test
    public void lesson_whenCreatingALesson_andTheMaxNumberOfAttendantsIsNegative_thenShouldThrowAnUnsupportedOperationException() {
        // Arrange
        var lesson = new Lesson();
        var command = new LessonCommand.CreateLessonCommand(
                UUID.randomUUID(),
                DATE_IN_THE_FUTURE,
                START_TIME,
                END_TIME,
                -1,
                EMPTY_LESSONS_LIST);

        // Act and assert
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> lesson.handle(command));
    }
}
