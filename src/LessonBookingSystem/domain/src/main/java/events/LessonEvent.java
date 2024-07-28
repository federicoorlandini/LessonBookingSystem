package events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDateTime;
import java.util.UUID;

// This interface defines the events for the model.Lesson
public sealed interface LessonEvent {
     UUID getEventId();
     UUID getLessonId();
     LocalDateTime getDateAndTime();
     int getMaxNumberAttenders();

     record LessonCreated(
             UUID eventId,
             UUID lessonId,
             LocalDateTime dateAndTime,
             int maxNumberAttenders) implements LessonEvent {
          @Override
          public UUID getEventId() {
               return eventId;
          }

          @Override
          public UUID getLessonId() {
               return lessonId;
          }

          @JsonSerialize(using = LocalDateSerializer.class)
          @Override
          public LocalDateTime getDateAndTime() {
               return dateAndTime;
          }

          @Override
          public int getMaxNumberAttenders() {
               return maxNumberAttenders;
          }
     };
}



