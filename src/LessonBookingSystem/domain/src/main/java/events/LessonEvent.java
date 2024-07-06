package events;

import java.time.LocalDateTime;
import java.util.UUID;

// This interface defines the events for the Lesson
public sealed interface LessonEvent {
     // Nothing here for now
     record LessonCreated(
             UUID eventId,
             LocalDateTime dateAndTime,
             int maxNumberAttenders) implements LessonEvent {};
}


