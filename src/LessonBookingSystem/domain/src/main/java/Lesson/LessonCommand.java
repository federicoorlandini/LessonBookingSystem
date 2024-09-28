package Lesson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public sealed interface LessonCommand {
    record CreateLessonCommand(
        UUID lessonId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        int maxNumberAttenders,
        List<Lesson> otherLessonsOnSameDay) implements LessonCommand { }
}
