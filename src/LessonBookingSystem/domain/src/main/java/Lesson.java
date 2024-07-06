import events.LessonEvent;

import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.UUID;

// Aggregate Lesson
public class Lesson {
    private final UUID id;
    private LocalDateTime dateAndTime;
    private int maxNumberAttenders;

    public Lesson(UUID id) {
        this.id = id;
    }

    public UUID id() {
        return id;
    }

    public LocalDateTime dateAndTime() {
        return dateAndTime;
    }

    public int maxNumberAttenders() {
        return maxNumberAttenders;
    }

    public void when(LessonEvent event) throws InvalidClassException {
        if (event instanceof LessonEvent.LessonCreated) {
            var e = (LessonEvent.LessonCreated)event;
            // Here we must put the logic to change the status of the aggregate
        } else {
            // Unknown event type
            throw new InvalidClassException("Invalid type found: " + event.getClass().getName());
        }
    }
}
