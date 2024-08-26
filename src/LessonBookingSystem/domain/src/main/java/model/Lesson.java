package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import commands.LessonCommand;
import events.LessonEvent;

import java.io.InvalidClassException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Aggregate model.Lesson
public class Lesson {
    public enum Status { OPEN, CANCELED }

    private UUID id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int maxNumberAttenders;
    private Status status;

    public UUID getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    @JsonSetter("date")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() {
        return startTime;
    }

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() {
        return endTime;
    }

    @JsonDeserialize(using = LocalTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public int getMaxNumberAttenders() {
        return maxNumberAttenders;
    }

    public Status getStatus() { return status; }

    // Create command handler
    public List<LessonEvent> handle(LessonCommand.CreateLessonCommand command) throws InvalidClassException {
        // Command handlers should only validate the command and not change the status.
        // Only events should change the aggregate status

        // Validation:
        var startDateAndTime = command.startTime().atDate(command.date());
        if( startDateAndTime.isBefore(LocalDateTime.now())) {
            throw new UnsupportedOperationException("The date and time for the begin of the lesson is in the past");
        }

        if ( command.maxNumberAttenders() <= 0 ){
            throw new UnsupportedOperationException("The max number of attenders must be positive");
        }

        // Generate the event
        var events = new ArrayList<LessonEvent>();
        events.add(
                new LessonEvent.LessonCreated(
                        UUID.randomUUID(),
                        command.lessonId(),
                        1,
                        command.date(),
                        command.startTime(),
                        command.endTime(),
                        command.maxNumberAttenders()));

        // Use the events to evolve the aggregate status
        evolve(events);

        return events;
    }

    public void evolve(List<LessonEvent> events) throws InvalidClassException {
        for (LessonEvent e : events) {
            evolve(e);
        }
    }

    public void evolve(LessonEvent event) throws InvalidClassException {
        if (event instanceof LessonEvent.LessonCreated e) {
            this.id = e.lessonId();
            this.date = e.date();
            this.startTime = e.startTime();
            this.endTime = e.endTime();
            this.maxNumberAttenders = e.maxNumberAttenders();
            this.status = Status.OPEN;
        } else {
            // Unknown event type
            throw new InvalidClassException("Invalid type found: " + event.getClass().getName());
        }
    }
}
