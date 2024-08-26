package events;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

// This interface defines the events for the model.Lesson
public sealed interface LessonEvent {
     UUID eventId();
     UUID lessonId();
     int version();

     default String eventStreamId() {
          return MessageFormat.format("Lesson-{0}", lessonId());
     }

     record LessonCreated(
             UUID eventId,
             UUID lessonId,
             int version,
             LocalDate date,
             LocalTime startTime,
             LocalTime endTime,
             int maxNumberAttenders) implements LessonEvent {

          @JsonSerialize(using = LocalDateSerializer.class)
          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
          @Override
          public LocalDate date() {
               return date;
          }

          @JsonSerialize(using = LocalTimeSerializer.class)
          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
          @Override
          public LocalTime endTime() {
               return endTime;
          }

          @JsonSerialize(using = LocalTimeSerializer.class)
          @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
          @Override
          public LocalTime startTime() {
               return startTime;
          }
     };
}



