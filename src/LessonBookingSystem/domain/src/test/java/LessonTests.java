import commands.LessonCommand;
import events.LessonEvent;
import model.Lesson;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.UUID;

public class LessonTests {
    @Test
    public void lesson_whenCreatingALesson_theAggregateSetupShouldBeCorrect() throws InvalidClassException {
        // Arrange
        var lesson = new Lesson();
        var command = new LessonCommand.CreateLessonCommand(
                UUID.randomUUID(),
                LocalDateTime.now().plusDays(1),
                10);

        // Act
        lesson.handle(command);

        // Assert
        assertThat(lesson.getId()).isEqualByComparingTo(command.lessonId());
        assertThat(lesson.getDateAndTime()).isEqualToIgnoringNanos(command.dateAndTime());
        assertThat(lesson.getMaxNumberAttenders()).isEqualTo(command.maxNumberAttenders());
    }

    @Test
    public void lesson_whenCreatingALesson_shouldGenerateACorrectLessonCreatedEvent() throws InvalidClassException {
        // Arrange
        var lesson = new Lesson();
        var command = new LessonCommand.CreateLessonCommand(
                UUID.randomUUID(),
                LocalDateTime.now().plusDays(1),
                10);

        // Act
        var events = lesson.handle(command);

        // Assert
        assertThat(events).hasSize(1);
        var event = events.getFirst();
        assertThat(event).isInstanceOf(LessonEvent.LessonCreated.class);
        assertThat(event.eventId()).isNotNull();
        assertThat(event.lessonId()).isEqualByComparingTo(command.lessonId());
        var createdEvent = (LessonEvent.LessonCreated)event;
        assertThat(createdEvent.dateAndTime()).isEqualToIgnoringNanos(command.dateAndTime());
        assertThat(createdEvent.maxNumberAttenders()).isEqualTo(command.maxNumberAttenders());
    }

    @Test
    public void lesson_whenCreatingALesson_andTheDateIsInThePast_thenShouldThrowAnUnsupportedOperationException() {
        // Arrange
        var lesson = new Lesson();
        var command = new LessonCommand.CreateLessonCommand(
                UUID.randomUUID(),
                LocalDateTime.now().minusDays(1),
                10);

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
                LocalDateTime.now(),
                0);

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
                LocalDateTime.now(),
                -1);

        // Act and assert
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> lesson.handle(command));
    }
}
