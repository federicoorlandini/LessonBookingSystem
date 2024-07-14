package events;

import java.time.LocalDateTime;
import java.util.UUID;

// This interface defines the events for the model.Lesson
public sealed interface LessonEvent {
     record LessonCreated(
             UUID lessonId,
             LocalDateTime dateAndTime,
             int maxNumberAttenders) implements LessonEvent {};
}


