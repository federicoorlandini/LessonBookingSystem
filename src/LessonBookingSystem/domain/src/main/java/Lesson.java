import commands.LessonCommand;
import events.LessonEvent;

import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.UUID;

// Aggregate Lesson
public class Lesson {
    private UUID id;
    private LocalDateTime dateAndTime;
    private int maxNumberAttenders;

    public static Lesson initialize() {
        return new Lesson();
    }

    private Lesson() {
        id = UUID.randomUUID();
        dateAndTime = null;
        maxNumberAttenders = 0;
    }

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

    public void handle(LessonCommand.CreateLessonCommand command) {
        // Command handlers should only validate the command and not change the status.
        // Only events should change the aggregate status

        // Validation:
        //  - is the date in the future?
        //  - is the max number of attendents positive?

        // Generate the event

    }

    public void evolve(LessonEvent event) throws InvalidClassException {
        if (event instanceof LessonEvent.LessonCreated) {
            var e = (LessonEvent.LessonCreated)event;
            this.dateAndTime = e.dateAndTime();
            this.maxNumberAttenders = e.maxNumberAttenders();
        } else {
            // Unknown event type
            throw new InvalidClassException("Invalid type found: " + event.getClass().getName());
        }
    }
}
