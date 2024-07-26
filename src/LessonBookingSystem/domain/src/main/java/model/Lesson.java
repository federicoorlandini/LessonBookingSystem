package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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

    public List<LessonEvent> handle(LessonCommand.CreateLessonCommand command) {
        // Command handlers should only validate the command and not change the status.
        // Only events should change the aggregate status

        // Validation:
        //  - is the date in the future?
        //  - is the max number of attendents positive?

        // Generate the event
        var events = new ArrayList<LessonEvent>();
        events.add(new LessonEvent.LessonCreated(UUID.randomUUID(), command.id(), command.dateAndTime(), command.maxNumberAttenders()));
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
