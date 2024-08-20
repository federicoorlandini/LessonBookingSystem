package commands;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public sealed interface LessonCommand {
    record CreateLessonCommand(
        UUID lessonId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        int maxNumberAttenders) implements LessonCommand { }
}
