package model;

import commands.LessonCommand;
import events.LessonEvent;

import java.io.InvalidClassException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Aggregate model.Lesson
public class Lesson {
    private UUID id;
    private LocalDateTime dateAndTime;
    private int maxNumberAttenders;

    public UUID getId() {
        return id;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public int getMaxNumberAttenders() {
        return maxNumberAttenders;
    }

    public List<LessonEvent> handle(LessonCommand.CreateLessonCommand command) throws InvalidClassException {
        // Command handlers should only validate the command and not change the status.
        // Only events should change the aggregate status

        // Validation:
        if( command.dateAndTime().isBefore(LocalDateTime.now())) {
            throw new UnsupportedOperationException("The date and time for the lesson is in the past");
        }

        if ( command.maxNumberAttenders() <= 0 ){
            throw new UnsupportedOperationException("The max number of attenders must be positive");
        }

        // Generate the event
        var events = new ArrayList<LessonEvent>();
        events.add(new LessonEvent.LessonCreated(UUID.randomUUID(), command.lessonId(), command.dateAndTime(), command.maxNumberAttenders()));

        evolve(events);
        return events;
    }

    public void evolve(List<LessonEvent> events) throws InvalidClassException {
        for (LessonEvent e : events) {
            evolve(e);
        }
    }

    public void evolve(LessonEvent event) throws InvalidClassException {
        if (event instanceof LessonEvent.LessonCreated) {
            var e = (LessonEvent.LessonCreated)event;
            this.id = e.lessonId();
            this.dateAndTime = e.dateAndTime();
            this.maxNumberAttenders = e.maxNumberAttenders();
        } else {
            // Unknown event type
            throw new InvalidClassException("Invalid type found: " + event.getClass().getName());
        }
    }
}
