package commands;

import java.time.LocalDateTime;
import java.util.UUID;

public sealed interface LessonCommand {
    record CreateLessonCommand(
        UUID id,
        LocalDateTime dateAndTime,
        int maxNumberAttenders) implements LessonCommand { }
}
